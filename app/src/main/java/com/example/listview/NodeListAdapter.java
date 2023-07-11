package com.example.listview;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NodeListAdapter extends ArrayAdapter<NodeData> {

    private static final String TAG = "NodeListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private static class ViewHolder {
        TextView node;
        TextView ID;
        TextView X;
        TextView Y;
        TextView Z;

    }

    public NodeListAdapter(Context context, int resource, ArrayList<NodeData> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the Node information
        String node = getItem(position).getNode();
        String ID = getItem(position).getID();
        double X = getItem(position).getX();
        double Y = getItem(position).getY();
        double Z = getItem(position).getZ();

        //Create the Node object with the information
        NodeData nodeData = new NodeData(node,ID,X,Y,Z);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.node = (TextView) convertView.findViewById(R.id.node);
            holder.ID = (TextView) convertView.findViewById(R.id.ID);
            holder.X = (TextView) convertView.findViewById(R.id.X);
            holder.Y = (TextView) convertView.findViewById(R.id.Y);
            holder.Z = (TextView) convertView.findViewById(R.id.Z);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }



        holder.node.setText(nodeData.getNode());
        holder.ID.setText(nodeData.getID());
        holder.X.setText(String.valueOf(nodeData.getX()));
        holder.Y.setText(String.valueOf(nodeData.getY()));
        holder.Z.setText(String.valueOf(nodeData.getZ()));



        return convertView;
    }
}
























