package com.example.scalc.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scalc.Model.Jornada;
import com.example.scalc.Model.Usuario;
import com.example.scalc.R;

import java.util.List;
import java.util.Locale;

public class JornadaAdapter extends RecyclerView.Adapter<JornadaAdapter.JornadaViewHolder> {

    private List<Jornada> listaJornadas;
    private Usuario usuario;

    public JornadaAdapter(List<Jornada> listaJornadas, Usuario usuario) {
        this.listaJornadas = listaJornadas;
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public JornadaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jornada, parent, false);
        return new JornadaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JornadaViewHolder holder, int position) {
        Jornada jornada = listaJornadas.get(position);

        holder.tvFecha.setText(String.valueOf(jornada.getFecha()));
        holder.tvPedidos.setText(jornada.getCant_pedidos() + " Pedidos");
        holder.tvHoras.setText(String.format(Locale.getDefault(), "%.1f h", jornada.getCant_horas()));

        double dineroDia = (jornada.getCant_horas() * usuario.getTarifa_hora())
                + (jornada.getCant_pedidos() * usuario.getTarifa_pedido());

        holder.tvDinero.setText(String.format(Locale.getDefault(), "%.2f €", dineroDia));
    }

    @Override
    public int getItemCount() {
        return listaJornadas.size();
    }

    public static class JornadaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvPedidos, tvHoras, tvDinero;

        public JornadaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvJornadaFecha);
            tvPedidos = itemView.findViewById(R.id.tvJornadaPedidos);
            tvHoras = itemView.findViewById(R.id.tvJornadaHoras);
            tvDinero = itemView.findViewById(R.id.tvJornadaDinero); // ¡Nuevo ID!
        }
    }
}