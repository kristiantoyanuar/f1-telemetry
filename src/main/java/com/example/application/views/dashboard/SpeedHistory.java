package com.example.application.views.dashboard;

import com.example.application.data.entity.Car;
import com.example.application.data.service.CarService;
import com.example.application.event.SpeedChangedEvent;
import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;

public class SpeedHistory extends VerticalLayout {
    private Car car;
    long lastUpdated;

    long attachedTime;
    private Chart chart;

    public SpeedHistory(ConfigurableApplicationContext applicationContext, Car car, AuthenticatedUser authenticatedUser) {
        this.car = car;
        chart = new Chart();
        chart.setTimeline(true);
        Configuration configuration = chart.getConfiguration();
        configuration.setTitle("Speed History");

        PlotLine plotLine = new PlotLine();
        plotLine.setValue(2);
        plotLine.setWidth(2);

        XAxis timeAxis = configuration.getxAxis();
        timeAxis.setTitle("Time");
        timeAxis.setTickInterval(10000);

        YAxis speedAxis = configuration.getyAxis();
        speedAxis.setTitle("Speed");
        speedAxis.setMin(0);
        speedAxis.setMax(350);
        speedAxis.setTickInterval(50);

        RangeSelector rangeSelector = new RangeSelector();
        rangeSelector.setSelected(5);

        PlotOptionsSeries plotOptionsSeries = new PlotOptionsSeries();
        configuration.setPlotOptions(plotOptionsSeries);

        H2 h2 = new H2("Speed History: Car " + car.getName());
        h2.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");
        DataSeries series = new DataSeries(car.getName());
        applicationContext.getBean(CarService.class).getSpeed(authenticatedUser, car)
                .stream()
                .forEach(speed -> series.add(new DataSeriesItem(speed.getTimestamp().toInstant(), speed.getSpeed())));
        configuration.setSeries(series);

        ApplicationListener<SpeedChangedEvent> speedChangeListener = event -> {
            if (car.getId().equals(event.getCar().getId())) {
                // push to UI
                getUI().ifPresent(ui -> {
                    // need to increase the update frequency since 10ms doesn't make sense visually
                    if (System.currentTimeMillis() - lastUpdated >= 500) {
                        ui.access(() -> {
                            series.add(new DataSeriesItem(event.getCarSpeed().getTimestamp().toInstant(), event.getNewSpeed()), true, true);
                            lastUpdated = System.currentTimeMillis();
                        });
                    }
                });
            }
        };
        // attach speed listener
        addAttachListener(attachEvent -> {
            applicationContext.addApplicationListener(speedChangeListener);
            attachedTime = System.currentTimeMillis();
        });
        // detach speed listener
        addDetachListener(detachEvent -> {
            ApplicationEventMulticaster aem =
                    applicationContext.getBean(
                            AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                            ApplicationEventMulticaster.class);
            aem.removeApplicationListener(speedChangeListener);
        });

        add(h2, chart);
    }

}
