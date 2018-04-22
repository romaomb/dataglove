package romao.matheus.dataglove.utilities

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20
import android.view.MotionEvent
import org.rajawali3d.Object3D
import org.rajawali3d.cameras.ArcballCamera
import org.rajawali3d.materials.Material
import org.rajawali3d.materials.textures.ATexture
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.primitives.Cylinder
import org.rajawali3d.primitives.RectangularPrism
import org.rajawali3d.primitives.Sphere
import org.rajawali3d.renderer.Renderer
import org.rajawali3d.view.SurfaceView
import romao.matheus.dataglove.R
import romao.matheus.dataglove.fragments.SimulationFragment

class RajawaliRenderer(context: Context, private val surface: SurfaceView) : Renderer(context) {

    private var hand: Object3D? = null
    private val mcp = ArrayList<Object3D>()
    private val proximalPhalanxes = ArrayList<Object3D>()
    private val pip = ArrayList<Object3D>()
    private val intPhalanxes = ArrayList<Object3D>()
    private val dip = ArrayList<Object3D>()
    private val distalPhalanxes = ArrayList<Object3D>()
    private val thumb = ArrayList<Object3D>()
    private val handJoints = ArrayList<Object3D>()
    private var materialPhalanxes: Material? = null
    private var materialJoints: Material? = null

    public override fun initScene() {
        cameraDefault()
        try {
            currentScene.setSkybox(R.drawable.posx2, R.drawable.negx2, R.drawable.posy2, R.drawable.negy2, R.drawable.posz2, R.drawable.negz2)
        } catch (e: ATexture.TextureException) {
            e.printStackTrace()
        }

        //Global Materials
        materialPhalanxes = Material()
        materialPhalanxes!!.color = Color.GRAY
        materialJoints = Material()
        materialJoints!!.color = Color.WHITE

        //Torso
        val materialTorso = Material()
        materialTorso.color = Color.BLACK
        materialTorso.colorInfluence = 0f
        hand = RectangularPrism(11f, 2f, 11f)
        hand!!.setPosition(0.0, 0.0, 0.0)
        hand!!.material = materialTorso
        currentScene.addChild(hand)

        //Distance in Z-axis
        val phalanxesLength = floatArrayOf(4f, 5f, 5.5f, 4.5f) //{3.5f, 4.5f, 4f, 3f};
        val distanceZ = FloatArray(4)
        val distanceX = FloatArray(4)
        for (i in 0..3) {
            distanceZ[i] = -(phalanxesLength[i] / 2 + 1)
            distanceX[i] = 4.5f - i * 3 //(i*3)+4.5f;
        }

        //Create Fingers II - V
        for (i in 0..3) {
            mcp.add(newJoint(hand!!, distanceX[i], -5f))
            proximalPhalanxes.add(newPhalanx(mcp[i], phalanxesLength[i], distanceZ[i]))
            pip.add(newJoint(proximalPhalanxes[i], 0f, distanceZ[i]))
            intPhalanxes.add(newPhalanx(pip[i], phalanxesLength[i], distanceZ[i]))
            dip.add(newJoint(intPhalanxes[i], 0f, distanceZ[i]))
            distalPhalanxes.add(newPhalanx(dip[i], phalanxesLength[i], distanceZ[i]))
        }

        //Create Thumb
        val thumbLength = 6f
        thumb.add(newJoint(hand!!, -6.5f, 5f))
        thumb[0].rotate(Vector3.Axis.Y, -10.0)
        thumb.add(newPhalanx(thumb[0], thumbLength, -(thumbLength / 2 + 1)))
        thumb.add(newJoint(thumb[1], 0f, -(thumbLength / 2 + 1)))
        thumb[2].rotate(Vector3.Axis.Y, 7.0)
        thumb.add(newPhalanx(thumb[2], thumbLength / 2, -(thumbLength / 4 + 1)))
        thumb.add(newJoint(thumb[3], 0f, -(thumbLength / 4 + 1)))
        thumb[4].rotate(Vector3.Axis.Y, 3.0)
        thumb.add(newPhalanx(thumb[4], thumbLength / 2, -(thumbLength / 4 + 1)))
    }

    private fun newPhalanx(joint: Object3D, length: Float, distance: Float): Object3D {
        val bone = Cylinder(length, 1f, 24, 24, true, true, true)
        bone.setPosition(0.0, 0.0, distance.toDouble())
        bone.isDoubleSided = true
        bone.drawingMode = GLES20.GL_TRIANGLE_FAN
        bone.material = materialPhalanxes
        joint.addChild(bone)
        return bone
    }

    private fun newJoint(phalanxes: Object3D, distanceX: Float, distanceZ: Float): Object3D {
        val joint = Sphere(1f, 24, 24)
        joint.setPosition(distanceX.toDouble(), 0.0, distanceZ.toDouble())
        joint.material = materialJoints
        phalanxes.addChild(joint)
        handJoints.add(joint)
        return joint
    }

    private fun cameraDefault() {
        val arcball = ArcballCamera(mContext, surface)
        arcball.setPosition(75.0, 40.0, -30.0)
        arcball.farPlane = 1000.0
        currentScene.replaceAndSwitchCamera(currentCamera, arcball)
    }

    override fun onRender(ellapsedRealtime: Long, deltaTime: Double) {
        super.onRender(ellapsedRealtime, deltaTime)
        if (SimulationFragment.sensorList.sensors.isNotEmpty()) {
            for (i in 0..14) {
                handJoints[i].setRotation(Vector3.Axis.X, -SimulationFragment.sensorList.sensors[i].angle)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent) {}

    override fun onOffsetsChanged(x: Float, y: Float, z: Float, w: Float, i: Int, j: Int) {}
}
