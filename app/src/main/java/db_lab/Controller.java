package db_lab;

import db_lab.data.DAOException;
import db_lab.data.ProductPreview;
import db_lab.model.Model;
import java.util.Objects;

// The controller provides a holistic description of how the outside world can
// interact with our application: each public method is written as
//
//   subject + action + object (e.g. user + clicked + preview)
//
// So just by reading all the methods we know of all the possible interactions
// that can happen in our app. This makes it simpler to track all the possible
// actions that can take place as the application grows.
//
public final class Controller {

    // The controller holds a reference to the:
    //   - model: to have it load new data
    //   - view: to update it as new data is loaded
    //
    //    ┌────── updates ──────┐
    //    │                     │
    // ┌──▼┐                 ┌─┴────────┐ updates ┌──────┐
    // │view│                 │controller├─────────►model│
    // └──┬─┘                 └─▲───────┘         └──────┘
    //    │       notifies      │
    //    └────── of user's ────┘
    //            actions
    //
    private final Model model;
    private final View view;

    public Controller(Model model, View view) {
        Objects.requireNonNull(model, "Controller created with null model");
        Objects.requireNonNull(view, "Controller created with null view");
        this.view = view;
        this.model = model;
    }

    public void userRequestedInitialPage() {
        this.loadInitialPage();
    }

    public void userClickedReloadPreviews() {
        this.loadInitialPage();
    }

    public void userClickedPreview(ProductPreview productPreview) {
        try {
            this.view.loadingProduct();
            var product = this.model.find(productPreview.code);
            if (product.isPresent()) {
                this.view.productPage(product.get());
            } else {
                this.view.failedToLoadProduct(productPreview);
            }
        } catch (DAOException e) {
            this.view.failedToLoadProduct(productPreview);
        }
    }

    public void userClickedBack() {
        if (this.model.loadedPreviews()) {
            this.view.previewPage(this.model.previews());
        } else {
            this.loadInitialPage();
        }
    }

    void loadInitialPage() {
        try {
            this.view.loadingPreviews();
            var previews = this.model.loadPreviews();
            this.view.previewPage(previews);
        } catch (DAOException e) {
            e.printStackTrace();
            this.view.failedToLoadPreviews();
        }
    }
}
