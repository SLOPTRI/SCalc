package com.example.scalc.Adapter; // O el paquete donde guardes adaptadores

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scalc.DetalleTicketActivity;
import com.example.scalc.Model.Ticket;
import com.example.scalc.R;

import java.util.List;
import java.util.Locale;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<Ticket> listaTickets;

    // Constructor: Le pasamos la lista de datos
    public TicketAdapter(List<Ticket> listaTickets) {
        this.listaTickets = listaTickets;
    }

    // 1. Inflar el diseño (crear la vista visual)
    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    // 2. Asignar los datos a cada elemento
    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = listaTickets.get(position);

        // Ponemos Mes y Año en mayúsculas
        String titulo = ticket.getMes().toUpperCase() + " " + ticket.getAnio();
        holder.tvMes.setText(titulo);

        // Estado (Primera letra mayúscula)
        holder.tvEstado.setText(ticket.getEstado());

        // Formatear dinero 0000.00 €
        holder.tvDinero.setText(String.format(Locale.getDefault(), "%.2f €", ticket.getSalario_total()));

        // Resumen de abajo
        String resumen = ticket.getTotal_pedidos() + " Pedidos  •  " + ticket.getTotal_horas() + "h";
        holder.tvResumen.setText(resumen);

        holder.itemView.setOnClickListener(v -> {
            // 1. Obtenemos el contexto desde la propia vista (el botón/tarjeta)
            Context context = v.getContext();

            // 2. Creamos el Intent directamente aquí
            Intent intent = new Intent(context, DetalleTicketActivity.class);
            intent.putExtra("ID_TICKET_SELECCIONADO", ticket.getId());

            // 3. ¡Arrancamos!
            context.startActivity(intent);
        });
    }

    // 3. Cantidad de elementos
    @Override
    public int getItemCount() {
        return listaTickets.size();
    }

    // Clase interna para "agarrar" los elementos del XML
    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvMes, tvEstado, tvDinero, tvResumen;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMes = itemView.findViewById(R.id.tvItemMes);
            tvEstado = itemView.findViewById(R.id.tvItemEstado);
            tvDinero = itemView.findViewById(R.id.tvItemDinero);
            tvResumen = itemView.findViewById(R.id.tvItemResumen);
        }
    }
}