package ru.shop.makstore.views;

import com.vaadin.flow.theme.lumo.LumoUtility;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();

        H1 title = new H1("MakStore");
        title.addClassNames(LumoUtility.AlignSelf.END);
        add(title);

        HorizontalLayout menu = new HorizontalLayout();
        menu.add(createAdminMenu(), createUserMenu());
        add(menu);
    }

    private VerticalLayout createAdminMenu() {
        VerticalLayout adminMenu = new VerticalLayout();
        adminMenu.add(new RouterLink("Admin Products", AdminProductView.class));
        return adminMenu;
    }

    private VerticalLayout createUserMenu() {
        VerticalLayout userMenu = new VerticalLayout();
        userMenu.add(new RouterLink("User Products", UserProductView.class));
        return userMenu;
    }
}
