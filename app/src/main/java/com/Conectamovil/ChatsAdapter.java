package com.Conectamovil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.Conectamovil.Mensajeria;
import com.Conectamovil.R;

import java.util.List;

public class ChatsAdapter extends BaseAdapter {

    private Context context;
    private List<Mensajeria> chatsList;

    public ChatsAdapter(Context context, List<Mensajeria> chatsList) {
        this.context = context;
        this.chatsList = chatsList;
    }

    @Override
    public int getCount() {
        return chatsList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_chats, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.txtnombreMensaje = convertView.findViewById(R.id.txtnombreMensaje);
            viewHolder.txttipoMensaje = convertView.findViewById(R.id.txtcorreoContacto);
            viewHolder.txtContenido = convertView.findViewById(R.id.txtedadContacto);
            viewHolder.txttimestamp = convertView.findViewById(R.id.txtnacionalidadContacto);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Mensajeria chats = chatsList.get(position);

        viewHolder.txtnombreMensaje.setText("nombreMensaje: " + chats.getNombreMensaje());
        viewHolder.txttipoMensaje.setText("tipoMensaje: " + chats.getTipoMensaje());
        viewHolder.txtContenido.setText("Contenido: " + chats.getContenido());
        viewHolder.txttimestamp.setText("timestamp: " + chats.getTimestamp());

        return convertView;
    }

    private static class ViewHolder {
        TextView txtnombreMensaje;
        TextView txttipoMensaje;
        TextView txtContenido;
        TextView txttimestamp;
    }
}
