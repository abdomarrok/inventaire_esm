package com.marrok.inventaire_esm.controller.bon_retour;

import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.event.ActionEvent;

public class BonRetourController {
    public void goHome(ActionEvent event) {
        GeneralUtil.goBackStockDashboard(event);
    }

}
