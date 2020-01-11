package com.company;

class Node {
    // (x, y) represents matrix cell coordinates
    // dist represent its minimum distance from the source
    int x, y, dist = 0;

    // maintain a parent node for printing path
    Node parent;

    Node(int x, int y, int dist, Node parent) {
        this.x = x;
        this.y = y;
        this.dist = 0;
        this.parent = parent;
    }

    public void setParent(Node node){
        parent = node;
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + '}';
    }

}
