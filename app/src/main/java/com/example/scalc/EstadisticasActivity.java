package com.example.scalc;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scalc.Model.Ticket;
import com.example.scalc.SQL.AdminSQLiteOpenHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class EstadisticasActivity extends AppCompatActivity {

    private AdminSQLiteOpenHelper admin;
    private LineChart lineChart;
    private BarChart barChart;
    private PieChart pieChart;
    private AutoCompleteTextView spinnerMetricas;
    private Button btnCerrarEst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        // 1. Inicializamos DB y Vistas
        admin = new AdminSQLiteOpenHelper(this);

        lineChart = findViewById(R.id.lineChart);
        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);

        spinnerMetricas = findViewById(R.id.spinnerMetricas);
        btnCerrarEst = findViewById(R.id.btnCerrarEst);

        // 2. Configurar UI
        configurarSelector();

        btnCerrarEst.setOnClickListener(v -> finish());

        actualizarGrafica("Facturación");
    }

    private void configurarSelector() {
        String[] opciones = {"Facturación", "Pedidos", "Horas", "Pedidos por Hora"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner, opciones);
        spinnerMetricas.setAdapter(adapter);

        spinnerMetricas.setOnItemClickListener((parent, view, position, id) -> {
            String seleccion = (String) parent.getItemAtPosition(position);
            actualizarGrafica(seleccion);
        });
    }

    private void actualizarGrafica(String metrica) {
        List<Ticket> tickets = admin.getTickets();

        // Reset de visibilidad (Ocultamos todo antes de mostrar la elegida)
        lineChart.setVisibility(View.GONE);
        barChart.setVisibility(View.GONE);
        pieChart.setVisibility(View.GONE);

        switch (metrica) {
            case "Facturación":
                lineChart.setVisibility(View.VISIBLE);
                generarGraficaLineal(tickets, "Facturación", getResources().getColor(R.color.scalc_primary));
                break;
            case "Pedidos":
                pieChart.setVisibility(View.VISIBLE);
                generarGraficaCircular(tickets, true);
                break;
            case "Horas":
                pieChart.setVisibility(View.VISIBLE);
                generarGraficaCircular(tickets, false);
                break;
            case "Pedidos por Hora":
                lineChart.setVisibility(View.VISIBLE);
                generarGraficaLineal(tickets, "PxH", getResources().getColor(R.color.scalc_primary));
                break;
        }
    }

    private void generarGraficaLineal(List<Ticket> tickets, String tipo, int color) {
        // 1. PREPARACIÓN DE DATOS
        ArrayList<Entry> entries = new ArrayList<>();
        final ArrayList<String> etiquetasMeses = new ArrayList<>();

        for (int i = 0; i < tickets.size(); i++) {
            Ticket t = tickets.get(i);
            float valor;

            // Lógica para diferenciar Facturación de Pedidos por Hora
            if (tipo.equals("Facturación")) {
                valor = (float) t.getSalario_total();
            } else {
                // Validación para evitar división por cero
                valor = t.getTotal_horas() > 0 ? (float)(t.getTotal_pedidos() / t.getTotal_horas()) : 0f;
            }

            entries.add(new Entry(i, valor));
            etiquetasMeses.add(t.getMes().substring(0, 3)); // "Ene", "Feb"...
        }

        if (entries.isEmpty()) {
            lineChart.clear();
            return;
        }

        // 2. CONFIGURACIÓN DE LA LÍNEA (DataSet)
        LineDataSet dataSet = new LineDataSet(entries, tipo);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setLineWidth(3f);      // Grosor de la línea
        dataSet.setCircleRadius(5f);   // Tamaño del punto
        dataSet.setDrawValues(true);   // Mostrar números sobre los puntos
        dataSet.setValueTextColor(getResources().getColor(R.color.text_primary));
        dataSet.setValueTextSize(10f);

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        // 3. ASIGNAR DATOS AL CHART
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        int blancoNegro = getColor(R.color.black);

        // --- EJE X (Inferior) ---
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setTextColor(blancoNegro);
        xAxis.setAxisLineColor(blancoNegro);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < etiquetasMeses.size()) {
                    return etiquetasMeses.get(index);
                }
                return "";
            }
        });

        // --- EJE Y (Izquierdo) ---
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(blancoNegro);
        leftAxis.setAxisLineColor(blancoNegro);

        // --- EJE Y (Derecho) ---
        lineChart.getAxisRight().setEnabled(false);

        // --- GENERAL ---
        lineChart.getDescription().setEnabled(false); // Quita el texto de descripción
        lineChart.getLegend().setEnabled(false);      // Quita la leyenda de colores de abajo
        lineChart.setExtraOffsets(10, 0, 10, 10);     // Márgenes extra para que no se corten los puntos

        // 5. REDIBUJAR
        lineChart.animateX(1000); // Animación suave al aparecer
        lineChart.invalidate();
    }

    private void generarGraficaCircular(List<Ticket> tickets, boolean esPedidos) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (Ticket t : tickets) {
            float valor = esPedidos ? (float) t.getTotal_pedidos() : (float) t.getTotal_horas();
            if (valor > 0) {
                entries.add(new PieEntry(valor, t.getMes()));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, esPedidos ? "Pedidos" : "Horas");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.setCenterText(esPedidos ? "Total Pedidos" : "Total Horas");
        pieChart.animateY(1000);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
    }
}