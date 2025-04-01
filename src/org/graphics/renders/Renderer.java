package org.graphics.renders;

import static org.lwjgl.opengl.GL46.*;

import org.graphics.utils.ShaderProgram;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

public class Renderer {
    private int vaoID, vboID, eboID;
    private ShaderProgram shaderProgram;

    public void init() {
        shaderProgram = new ShaderProgram("res/shaders/vertexShader.vert", "res/shaders/fragmentShader.frag");

        glEnable(GL_DEPTH_TEST);

        float[] vertices = {
                // X, Y, Z,  R, G, B
                -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, //0
                -0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 0.0f, //1 //Отпред
                0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, //2

                -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, //3
                -0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 0.0f,  //4 // Отзад

                -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, //5
                -0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f,  //6 //отдолу
                0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, //7

                -0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, //8
                -0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, //9 //отгоре

                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f,//10
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f,//11
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, //12 отстрани
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, //13


                0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, //14
                0.5f, -0.5f, 0.5f, 0.0f, 1.0f, 0.0f,//15 //Отпред

                0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, //16
                0.5f, -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, //17 // Отзад

                0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, //18
                0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, //19//отдолу

                0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, //20
                0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, //21//отгоре

                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, //22
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, //23
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, //24//отстрани
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, //25

        };

        int[] inidices = {
                0,1,2,
                2,3,4,
                5,6,7,
                7,8,9,
                10,11,12,
                11,12,13,
                2,14,15,
                2,16,17,
                7,18,19,
                7,20,21,
                22,23,24,
                23,24,25
        };
        
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(inidices.length);
        indexBuffer.put(inidices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indexBuffer,GL_STATIC_DRAW);

        // Атрибут 0 - позиция
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Атрибут 1 - цвят
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);


        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        // A=1, B=6, C=1, D=4
        shaderProgram.use();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glBindVertexArray(vaoID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        Matrix4f modelMatrix = new Matrix4f().identity();
        Matrix4f viewMatrix = new Matrix4f().lookAt(1.0f,0.0f,2.f,0.0f,0.0f,0.0f,0.0f,1.0f,0.0f);
        Matrix4f projectionMatrix = new Matrix4f().perspective((float)Math.toRadians(61),1.0f,0.1f,100.0f);
        shaderProgram.setUniform("modelMatrix",modelMatrix);
        shaderProgram.setUniform("viewMatrix",viewMatrix);
        shaderProgram.setUniform("projectionMatrix",projectionMatrix);

        glDrawElements(GL_TRIANGLES,36,GL_UNSIGNED_INT,0);
        glBindVertexArray(0);
    }
}