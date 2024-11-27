import socket
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
from Crypto.Random import get_random_bytes

class SiFT_MTP_Error(Exception):
    def __init__(self, err_msg):
        self.err_msg = err_msg

class SiFT_MTP:
    def __init__(self, peer_socket):
        self.DEBUG = True
        # --------- CONSTANTS ------------
        self.version_major = 0
        self.version_minor = 5
        self.msg_hdr_ver = b'\x00\x05'
        self.size_msg_hdr = 6
        self.size_msg_hdr_ver = 2
        self.size_msg_hdr_typ = 2
        self.size_msg_hdr_len = 2
        self.type_login_req =    b'\x00\x00'
        self.type_login_res =    b'\x00\x10'
        self.type_command_req =  b'\x01\x00'
        self.type_command_res =  b'\x01\x10'
        self.type_upload_req_0 = b'\x02\x00'
        self.type_upload_req_1 = b'\x02\x01'
        self.type_upload_res =   b'\x02\x10'
        self.type_dnload_req =   b'\x03\x00'
        self.type_dnload_res_0 = b'\x03\x10'
        self.type_dnload_res_1 = b'\x03\x11'
        self.msg_types = (self.type_login_req, self.type_login_res, 
                          self.type_command_req, self.type_command_res,
                          self.type_upload_req_0, self.type_upload_req_1, self.type_upload_res,
                          self.type_dnload_req, self.type_dnload_res_0, self.type_dnload_res_1)
        # --------- STATE ------------
        self.peer_socket = peer_socket
        self.transfer_key = None
        self.nonce_size = 12  # Recommended nonce size for AES-GCM

    # Method to set the transfer key after login
    def set_transfer_key(self, key):
        self.transfer_key = key

    # Parses a message header and returns a dictionary containing the header fields
    def parse_msg_header(self, msg_hdr):
        parsed_msg_hdr, i = {}, 0
        parsed_msg_hdr['ver'], i = msg_hdr[i:i+self.size_msg_hdr_ver], i+self.size_msg_hdr_ver 
        parsed_msg_hdr['typ'], i = msg_hdr[i:i+self.size_msg_hdr_typ], i+self.size_msg_hdr_typ
        parsed_msg_hdr['len'] = msg_hdr[i:i+self.size_msg_hdr_len]
        return parsed_msg_hdr

    # Receives n bytes from the peer socket
    def receive_bytes(self, n):
        bytes_received = b''
        bytes_count = 0
        while bytes_count < n:
            try:
                chunk = self.peer_socket.recv(n - bytes_count)
            except:
                raise SiFT_MTP_Error('Unable to receive via peer socket')
            if not chunk: 
                raise SiFT_MTP_Error('Connection with peer is broken')
            bytes_received += chunk
            bytes_count += len(chunk)
        return bytes_received

    # Receives and parses message, returns msg_type and msg_payload
    def receive_msg(self):
        try:
            msg_hdr = self.receive_bytes(self.size_msg_hdr)
        except SiFT_MTP_Error as e:
            raise SiFT_MTP_Error('Unable to receive message header --> ' + e.err_msg)

        if len(msg_hdr) != self.size_msg_hdr: 
            raise SiFT_MTP_Error('Incomplete message header received')
        
        parsed_msg_hdr = self.parse_msg_header(msg_hdr)

        if parsed_msg_hdr['ver'] != self.msg_hdr_ver:
            raise SiFT_MTP_Error('Unsupported version found in message header')

        if parsed_msg_hdr['typ'] not in self.msg_types:
            raise SiFT_MTP_Error('Unknown message type found in message header')

        msg_len = int.from_bytes(parsed_msg_hdr['len'], byteorder='big')

        try:
            msg_body = self.receive_bytes(msg_len - self.size_msg_hdr)
        except SiFT_MTP_Error as e:
            raise SiFT_MTP_Error('Unable to receive message body --> ' + e.err_msg)

        # If transfer key is set and message is not a login message, decrypt the payload
        if self.transfer_key and parsed_msg_hdr['typ'] not in [self.type_login_req, self.type_login_res]:
            # Extract nonce and tag from the message body
            nonce = msg_body[:self.nonce_size]
            tag = msg_body[self.nonce_size:self.nonce_size + 16]  # AES-GCM tag is 16 bytes
            encrypted_payload = msg_body[self.nonce_size + 16:]

            cipher = AES.new(self.transfer_key, AES.MODE_GCM, nonce=nonce)
            try:
                msg_body = cipher.decrypt_and_verify(encrypted_payload, tag)
            except (ValueError, KeyError) as e:
                raise SiFT_MTP_Error('Unable to decrypt or verify message body --> ' + str(e))

        # DEBUG 
        if self.DEBUG:
            print('MTP message received (' + str(msg_len) + '):')
            print('HDR (' + str(len(msg_hdr)) + '): ' + msg_hdr.hex())
            print('BDY (' + str(len(msg_body)) + '): ')
            print(msg_body.hex())
            print('------------------------------------------')
        # DEBUG 

        return parsed_msg_hdr['typ'], msg_body

    # Sends all bytes provided via the peer socket
    def send_bytes(self, bytes_to_send):
        try:
            self.peer_socket.sendall(bytes_to_send)
        except:
            raise SiFT_MTP_Error('Unable to send via peer socket')

    # Builds and sends message of a given type using the provided payload
    def send_msg(self, msg_type, msg_payload):
        # If transfer key is set and message is not a login message, encrypt the payload
        if self.transfer_key and msg_type not in [self.type_login_req, self.type_login_res]:
            nonce = get_random_bytes(self.nonce_size)
            cipher = AES.new(self.transfer_key, AES.MODE_GCM, nonce=nonce)
            encrypted_payload, tag = cipher.encrypt_and_digest(msg_payload)
            msg_payload = nonce + tag + encrypted_payload  # Concatenate nonce, tag, and ciphertext

        # Build message header and send as before...
        msg_size = self.size_msg_hdr + len(msg_payload)
        msg_hdr_len = msg_size.to_bytes(self.size_msg_hdr_len, byteorder='big')
        msg_hdr = self.msg_hdr_ver + msg_type + msg_hdr_len

        # DEBUG 
        if self.DEBUG:
            print('MTP message to send (' + str(msg_size) + '):')
            print('HDR (' + str(len(msg_hdr)) + '): ' + msg_hdr.hex())
            print('BDY (' + str(len(msg_payload)) + '): ')
            print(msg_payload.hex())
            print('------------------------------------------')
        # DEBUG 

        # Try to send
        try:
            self.send_bytes(msg_hdr + msg_payload)
        except SiFT_MTP_Error as e:
            raise SiFT_MTP_Error('Unable to send message to peer --> ' + e.err_msg)