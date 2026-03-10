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

    // 1. Declaramos la interfaz para escuchar el "mantener pulsado"
    private OnJornadaLongClickListener listener;

    public interface OnJornadaLongClickListener {
        void onJornadaLongClick(Jornada jornada, int position);
    }

    // 2. Actualizamos el constructor para que pida el listener
    public JornadaAdapter(List<Jornada> listaJornadas, Usuario usuario, OnJornadaLongClickListener listener) {
        this.listaJornadas = listaJornadas;
        this.usuario = usuario;
        this.listener = listener;
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

        // 3. Añadimos el evento de mantener pulsado sobre la tarjeta
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    // Usamos getAdapterPosition() para mayor seguridad si la lista cambia rápido
                    listener.onJornadaLongClick(jornada, holder.getAdapterPosition());
                }
                return true; // Importante: true significa que hemos consumido el evento
            }
        });
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
            tvDinero = itemView.findViewById(R.id.tvJornadaDinero);
        }
    }
}