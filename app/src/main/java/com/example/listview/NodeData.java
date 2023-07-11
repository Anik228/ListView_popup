package com.example.listview;


public class NodeData {
    private String node;
    private String id;
    private double X;
    private double Y;
    private double Z;

    public NodeData(String node, String id, double x, double y, double z) {
        this.node = node;
        this.id = id;
        X = x;
        Y = y;
        Z = z;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id= id;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double z) {
        Z = z;
    }
}