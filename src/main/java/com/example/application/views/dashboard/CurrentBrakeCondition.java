package com.example.application.views.dashboard;

import com.example.application.data.entity.Car;
import com.example.application.event.BrakeConditionChangedEvent;
import com.example.application.event.GearPositionChangedEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;

public class CurrentBrakeCondition extends VerticalLayout {
    private Car car;
    long lastUpdated;

    public CurrentBrakeCondition(ConfigurableApplicationContext applicationContext, Car car) {
        this.car = car;

        H2 h2 = new H2(car.getName() + " Brake Condition");
        h2.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");


        Span span = new Span("Condition 0");
        span.addClassNames("font-semibold", "text-3xl");

        add(h2, span);
        addClassName("p-l");
        setPadding(false);
        setSpacing(false);

        ApplicationListener<BrakeConditionChangedEvent> speedChangeListener = event -> {
            if (car.getId().equals(event.getCar().getId())) {
                // push to UI
                getUI().ifPresent(ui -> {
                    if (System.currentTimeMillis() - lastUpdated >= 10) {
                        ui.access(() -> {
                            lastUpdated = System.currentTimeMillis();
                            span.setText("Brake Condition: " + event.getNewCondition().name());
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
