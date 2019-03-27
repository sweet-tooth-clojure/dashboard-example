(ns dashboard-example.frontend.components.reports
  (:require [sweet-tooth.frontend.form.components :as stfc]
            [sweet-tooth.frontend.form.flow :as stff]
            [dashboard-example.cross.data :as cd]))

(defn report-list
  []
  [:div.report-list])

(defn report-form
  []
  (let [form-path                  [:reports :create]
        {:keys [input form-state]} (stfc/form form-path)]
    [:form.create-report (stfc/on-submit form-path)
     [:table
      [:tbody
       [:tr
        [:td "Movie Title"]
        [:td [input :text :report/title]]]
       [:tr
        [:td "Budget"]
        [:td [input :number :report/budget-2013-min {:placeholder "min"}]]
        [:td "-"]
        [:td [input :number :report/budget-2013-max {:placeholder "max"}]]]
       [:tr
        [:td "Domestic Gross"]
        [:td [input :number :report/domgross-2013-min {:placeholder "min"}]]
        [:td "-"]
        [:td [input :number :report/domgross-2013-max {:placeholder "max"}]]]
       [:tr
        [:td "Intl Gross"]
        [:td [input :number :report/intgross-2013-min {:placeholder "min"}]]
        [:td "-"]
        [:td [input :number :report/intgross-2013-max {:placeholder "max"}]]]
       [:tr
        [:td "Test Result"]
        [:td (map (fn [result]
                    ^{:key result}
                    [:label (name result) [input :checkbox-set :report/clean-test {:value result}]])
                  cd/test-result-options)]]
       [:tr
        [:td "Passes test?"]
        [:td
         [:label "yes" [input :radio :report/binary {:value true}]]
         [:label "no"  [input :radio :report/binary {:value false}]]
         [:span {:on-click #(stfc/dispatch-change [:reports :create] :report/binary nil)}
          "clear"]]]]]
     [:input {:type "submit"}]]))

(defn reports
  []
  [:div.reports
   [report-form]
   [report-list]])
