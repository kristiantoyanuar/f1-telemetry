package com.example.application.views.dashboard;

import com.example.application.data.entity.Car;
import com.example.application.event.SpeedChangedEvent;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;

public class CurrentSpeed extends VerticalLayout {
    private Car car;
    long lastUpdated;
    private Chart gauge;

    public CurrentSpeed(ConfigurableApplicationContext applicationContext, Car car) {
        this.car = car;
        gauge = new Chart(ChartType.GAUGE);
        gauge.setWidth("500px");
        Configuration conf = gauge.getConfiguration();
        conf.setTitle("Speed");
        conf.getPane().setStartAngle(-135);
        conf.getPane().setEndAngle(135);

        YAxis yaxis = new YAxis();
        yaxis.setTitle("km/h");
        yaxis.setMin(0);
        yaxis.setMax(350);
        yaxis.getLabels().setStep(1);
        yaxis.setTickInterval(50);
        yaxis.setTickLength(10);
        yaxis.setTickWidth(1);
        yaxis.setMinorTickInterval("1");
        yaxis.setMinorTickLength(10);
        yaxis.setMinorTickWidth(1);
        conf.addyAxis(yaxis);

        final ListSeries series = new ListSeries("Speed", 0);
        conf.addSeries(series);

        H2 h2 = new H2(car.getName() + " Current Speed");
        h2.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");

        add(h2, gauge);
        addClassName("p-l");
        setPadding(false);
        setSpacing(false);

        ApplicationListener<SpeedChangedEvent> speedChangeListener = event -> {
            if (car.getId().equals(event.getCar().getId())) {
                // push to UI
                getUI().ifPresent(ui -> {
                    // need to increase the update frequency since 10ms doesn't make sense visually
                    if (System.currentTimeMillis() - lastUpdated >= 200) {
                        ui.access(() -> {
                            lastUpdated = System.currentTimeMillis();
                            series.updatePoint(0, event.getNewSpeed());
                        });
                    }
                });
            }
        };

        // attach speed listener
        addAttachListener(attachEvent -> applicationContext.addApplicationListener(speedChangeListener));
        // detach speed listener
        addDetachListener(detachEvent -> {
            ApplicationEventMulticaster aem =
                    applicationContext.getBean(
                            AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                            ApplicationEventMulticaster.class);
            aem.removeApplicationListener(speedChangeListener);
        });
    }
}
