package com.example.matheus.dataglove;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.view.MotionEvent;
import android.widget.TextView;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.ArcballCamera;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cylinder;
import org.rajawali3d.primitives.RectangularPrism;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.SurfaceView;

class RajawaliRenderer extends Renderer {
    private org.rajawali3d.view.SurfaceView surface;
    private Object3D hand;
    private Object3D[] mcp = new Object3D[4];
    private Object3D[] proximalPhalanxes = new Object3D[4];
    private Object3D[] pip = new Object3D[4];
    private Object3D[] intPhalanxes = new Object3D[4];
    private Object3D[] dip = new Object3D[4];
    private Object3D[] distalPhalanxes = new Object3D[4];
    private Object3D[] thumb = new Object3D[6];
    private Object3D[] handJoints = new Object3D[16];
    private Material materialPhalanxes, materialJoints;
    private int qtd = 0;
    private float[] angles = new float[16];
    private TextView[] tabela;

    RajawaliRenderer(Context context, SurfaceView surface) {
        super(context);
        this.surface = surface;
    }

    @Override
    public void initScene() {
        cameraDefault();
        try {
            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy, R.drawable.negy, R.drawable.posz, R.drawable.negz);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        //Global Materials
        materialPhalanxes = new Material();
        materialPhalanxes.setColor(Color.GRAY);
        materialJoints = new Material();
        materialJoints.setColor(Color.WHITE);

        //Torso
        Material materialTorso = new Material();
        materialTorso.setColor(Color.BLACK);
        materialTorso.setColorInfluence(0);
        hand= new RectangularPrism(11f, 2f, 11f);
        hand.setPosition(0,0,0);
        hand.setMaterial(materialTorso);
        getCurrentScene().addChild(hand);

        //Distance in Z-axis
        float[] phalanxesLength = {4f, 5f, 5.5f, 4.5f}; //{3.5f, 4.5f, 4f, 3f};
        float[] distanceZ = new float[4];
        float[] distanceX = new float[4];
        for (int i = 0; i<4; i++) {
            distanceZ[i] = -((phalanxesLength[i]/2)+1);
            distanceX[i] = 4.5f - (i*3); //(i*3)+4.5f;
        }

        //Create Fingers II - V
        for (int i = 0; i<4; i++) {
            mcp[i] = newJoint(hand, distanceX[i], -5);
            proximalPhalanxes[i] = newPhalanx(mcp[i], phalanxesLength[i], distanceZ[i]);
            pip[i] = newJoint(proximalPhalanxes[i], 0, distanceZ[i]);
            intPhalanxes[i] = newPhalanx(pip[i], phalanxesLength[i], distanceZ[i]);
            dip[i] = newJoint(intPhalanxes[i], 0, distanceZ[i]);
            distalPhalanxes[i] = newPhalanx(dip[i], phalanxesLength[i], distanceZ[i]);
        }

        //Create Thumb
        float thumbLength = 6f;
        thumb[0] = newJoint(hand, -6.5f, 5f);
        thumb[0].rotate(Vector3.Axis.Y, -10f);
        thumb[1] = newPhalanx(thumb[0], thumbLength, -((thumbLength/2)+1));
        thumb[2] = newJoint(thumb[1], 0, -((thumbLength/2)+1));
        thumb[2].rotate(Vector3.Axis.Y, 7f);
        thumb[3] = newPhalanx(thumb[2], thumbLength/2, -((thumbLength/4)+1));
        thumb[4] = newJoint(thumb[3], 0, -((thumbLength/4)+1));
        thumb[4].rotate(Vector3.Axis.Y, 3f);
        thumb[5] = newPhalanx(thumb[4], thumbLength/2, -((thumbLength/4)+1));
    }

    void rotate(float[] angleY) {
       this.angles = angleY;
       for (int i = 0; i < 15; i++) {
           tabela[i].setText(String.valueOf(angles[i]));
       }
    }

    void setTabela(TextView[] tabela) {
        this.tabela = tabela;
    }

    Object3D newPhalanx(Object3D joint, float length, float distance) {
        Object3D bone = new Cylinder(length, 1, 24, 24, true, true, true);
        bone.setPosition(0, 0, distance);
        bone.setDoubleSided(true);
        bone.setDrawingMode(GLES20.GL_TRIANGLE_FAN);
        bone.setMaterial(materialPhalanxes);
        joint.addChild(bone);
        return bone;
    }

    Object3D newJoint(Object3D phalanxes, float distanceX, float distanceZ) {
        Object3D joint = new Sphere(1, 24, 24);
        joint.setPosition(distanceX,0, distanceZ);
        joint.setMaterial(materialJoints);
        phalanxes.addChild(joint);
        handJoints[qtd] = joint;
        qtd++;
        return joint;
    }

    void cameraDefault() {
        ArcballCamera arcball = new ArcballCamera(mContext, surface);
        arcball.setPosition(0, 0, 75);
        arcball.setFarPlane(1000);
        getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), arcball);
    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        for (int i = 0; i<15; i++) {
            handJoints[i].setRotation(Vector3.Axis.X, angles[i]);
        }
        hand.setRotation(Vector3.Axis.X, angles[15]);
    }

    @Override
    public void onTouchEvent(MotionEvent event){
    }

    @Override
    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j){
    }
}
