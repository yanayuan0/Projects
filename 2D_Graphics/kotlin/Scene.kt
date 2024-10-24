import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.MouseEvent
import org.khronos.webgl.WebGLRenderingContext as GL //# GL# we need this for the constants declared ˙HUN˙ a constansok miatt kell
import kotlin.js.Date
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Vec4
import kotlin.random.Random
import kotlin.math.atan2


class Scene (
  val gl : WebGL2RenderingContext){

  //var avatarPosX = 0.0f
  //val avatarPos = Vec3(0.5f, 0.5f, 0.0f)
  //val hedgeHogPos = Vec3(-0.8f, 0.0f, 0.0f)
  //val redTriPos = Vec3(0.8f, 0.0f, 0.0f)

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  val vsIdle = Shader(gl, GL.VERTEX_SHADER, "idle-vs.glsl")
  val fsSolid = Shader(gl, GL.FRAGMENT_SHADER, "solid-fs.glsl")
  val fsStriped = Shader(gl, GL.FRAGMENT_SHADER, "striped-fs.glsl")
  val stripedProgram = Program(gl, vsIdle, fsStriped, arrayOf("vertexPosition", "vertexColor"))
  val solidProgram = Program(gl, vsIdle, fsSolid, arrayOf("vertexPosition", "vertexColor"))

  val triGeometry = TriangleGeometry(gl)
  val heartGeometry = HeartGeometry(gl, 60)

  val stripedMaterialThick = Material (stripedProgram).apply{
    this["stripeWidth"]?.set (0.6f)
  }

   val stripedMaterialThin = Material (stripedProgram).apply{
    this["stripeWidth"]?.set (0.05f)
  }

  val solidMaterialRed = Material (solidProgram).apply{
    this["color"]?.set (0.8f, 0.1f, 0.05f)
  }

  val avatarMesh = Mesh (stripedMaterialThick, triGeometry)
  val stripedHeartMesh = Mesh (stripedMaterialThin, heartGeometry)

  val gameObjects = ArrayList<GameObject>()
  // var selectedObject: GameObject? = null
  val selectedObjects = mutableSetOf<GameObject>()
  val selectionMaterial = Material(solidProgram).apply {
    this["color"]?.set(1.0f, 1.0f, 0.0f) // Yellow color for selection
  }

  val staticGameObject = GameObject (stripedHeartMesh).apply {
      position.set (-0.5f, -0.5f, 0.0f)
  }
  init {
    gameObjects += staticGameObject

    for (i in 0..20) {
      gameObjects += GameObject (avatarMesh).apply {
        position.randomize(Vec3(-2.0f, -2.0f, 0.0f), Vec3(2.0f, 2.0f, 0.0f))
        scale.randomize(Vec3(0.1f, 0.1f, 1.0f), Vec3(0.3f, 0.3f, 1.0f))
        roll = Random.nextFloat() * (2.0f * 3.14f)
      }
    }
  }

  val camera = OrthoCamera()

  val avatar = GameObject (avatarMesh)
  init {
    gameObjects += avatar
  }
  
  fun resize(canvas : HTMLCanvasElement) {
    camera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())
    gl.viewport(0, 0, canvas.width, canvas.height)//#viewport# tell the rasterizer which part of the canvas to draw to ˙HUN˙ a raszterizáló ide rajzoljon
  }

  fun clickInScene(canvas: HTMLCanvasElement, screenX: Float, screenY: Float) {
    val ndc = Vec4(
        screenX * 2.0f / canvas.width.toFloat() - 1.0f,
        1.0f - screenY * 2.0f / canvas.height.toFloat(),
        0.0f,
        1.0f
    )
    val worldCoord = camera.viewProjMatrix.invert() * ndc
    var closestObject: GameObject? = null
    var minDistance = Float.MAX_VALUE
    val selectionThreshold = 0.1f

    for (gameObject in gameObjects) {
        val distance = (gameObject.position - worldCoord.xyz).length()
        if (distance < selectionThreshold && distance < minDistance) {
            closestObject = gameObject
            minDistance = distance
        }
    }

    if (closestObject == null) {
        selectedObjects.clear()
    } else {
        if (selectedObjects.contains(closestObject)) {
            selectedObjects.remove(closestObject)
        } else {
            selectedObjects.add(closestObject)
        }
    }
  }

  fun QFeature() {
    val selectedList = selectedObjects.toList()

    for (i in 0 until selectedList.size) {
        val currentObject = selectedList[i]
        val nextObject = selectedList[(i + 1) % selectedList.size]
        val direction = nextObject.position - currentObject.position
        val angle = atan2(direction.y.toDouble(), direction.x.toDouble()).toFloat()
        currentObject.roll = angle
    }
  }

  fun deleteSelectedObjects() {
    gameObjects.removeAll(selectedObjects)
    selectedObjects.clear()
  }
  /*fun clickInScene(canvas : HTMLCanvasElement, screenX: Float, screenY: Float) {
    val ndc = Vec4(screenX * 2.0f / canvas.width.toFloat() - 1.0f, 1.0f - screenY * 2.0f / canvas.height.toFloat(), 0.0f, 1.0f)
    avatar.position.set(ndc)
  }*/

  @Suppress("UNUSED_PARAMETER")
  fun update(keysPressed : Set<String>) {
    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t  = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f    
    timeAtLastFrame = timeAtThisFrame

    // if("RIGHT" in keysPressed){    
    //   //avatarPos.x +=  1.0f * dt
    // }
    selectedObjects.forEach { obj ->
        if ("RIGHT" in keysPressed) {
            obj.position.x += 1.0f * dt
        }
        if ("LEFT" in keysPressed) {
            obj.position.x -= 1.0f * dt
        }
        if ("UP" in keysPressed) {
            obj.position.y += 1.0f * dt
        }
        if ("DOWN" in keysPressed) {
            obj.position.y -= 1.0f * dt
        }
    }

    if("W" in keysPressed) {
      camera.position.y +=  1.0f * dt
    }
    if("A" in keysPressed) {
      camera.position.x -=  1.0f * dt
    }
    if("S" in keysPressed) {
      camera.position.y -=  1.0f * dt
    }
    if("D" in keysPressed) {
      camera.position.x +=  1.0f * dt
    }
    // if("Q" in keysPressed) {
    //   camera.roll +=  1.0f * dt
    // }
    if("E" in keysPressed) {
      camera.roll -=  1.0f * dt
    }
    if ("Q" in keysPressed && selectedObjects.size > 1) {
      QFeature()
    }
    if ("BACK_SPACE" in keysPressed && selectedObjects.size > 1) {
      deleteSelectedObjects()
    }

    if ("I" in keysPressed) {
      camera.position.y += 1.0f * dt
    }
    if ("J" in keysPressed) {
      camera.position.x -= 1.0f * dt
    }
    if ("K" in keysPressed) {
      camera.position.y -= 1.0f * dt
    }
    if ("L" in keysPressed) {
      camera.position.x += 1.0f * dt
    }


    gl.clearColor(0.3f, 0.0f, 0.3f, 1.0f)//## red, green, blue, alpha in [0, 1]
    gl.clearDepth(1.0f)//## will be useful in 3D ˙HUN˙ 3D-ben lesz hasznos
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)//#or# bitwise OR of flags

    camera.updateViewProjMatrix()

    //stripedMaterialThick.draw ()
    //avatarPos.commit (gl, gl.getUniformLocation(stripedProgram.glProgram,"gameObject.position")!!)
    //triGeometry.draw()
    //avatarMesh.draw ()
    //avatar.update ()
    //avatar.draw ()

    //stripedMaterialThin.draw ()
    //hedgeHogPos.commit (gl, gl.getUniformLocation(stripedProgram.glProgram,"gameObject.position")!!)
    //circleGeometry.draw()
    //stripedHedgehogMesh.draw ()

    //solidMaterialRed.draw ()
    //redTriPos.commit (gl, gl.getUniformLocation(solidProgram.glProgram,"gameObject.position")!!)
    //triGeometry.draw()
    //solidColoredTriMesh.draw ()
    //staticGameObject.update ()
    //staticGameObject.draw ()

    gameObjects.forEach{
      it.update()
    }
    // gameObjects.forEach { 
    //   it.draw(camera)
    // }
    gameObjects.forEach { gameObject ->
        if (selectedObjects.contains(gameObject)) {
            gameObject.using(selectionMaterial).draw(camera) // Draw selected objects with selection material
        } else {
            gameObject.draw(camera) // Draw other objects with their default material
        }
    }


  }
}
