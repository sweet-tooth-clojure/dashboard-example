(ns dashboard-example.frontend.components.app
  (:require [re-frame.core :as rf]
            [dashboard-example.frontend.subs :as ds]
            [dashboard-example.cross.data :as cd]
            [sweet-tooth.frontend.form.flow :as stff]
            [sweet-tooth.frontend.form.components :as stfc]))

(defprotocol TableFormat
  (format-cell [x]))

(extend-protocol TableFormat
  cljs.core/Keyword
  (format-cell [x] (name x))

  number
  (format-cell [x] (.toLocaleString x))

  string
  (format-cell [x] x)

  object
  (format-cell [x] x)

  boolean
  (format-cell [x] (if x "yes" "no"))

  nil
  (format-cell [x] nil))

(def fields
  [:movie/year :movie/imdb :movie/title :movie/clean-test :movie/binary
   :movie/budget-2013 :movie/domgross-2013 :movie/intgross-2013])

(defn report-form
  []
  (let [form-path                  [:report :create]
        {:keys [input form-state]} (stfc/form form-path)]
    [:form.create-report (stfc/on-submit form-path)
     [:table
      [:tbody
       [:tr
        [:td "Title"]
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
                    [:label (name result) [input :checkbox-set :report/clean-test {:value result}]])
                  cd/test-result-options)]]
       [:tr
        [:td "Passes test?"]
        [:td
         [:label "yes" [input :radio :report/binary {:value true}]]
         [:label "no"  [input :radio :report/binary {:value false}]]
         [:span {:on-click #(stfc/dispatch-change [:report :create] :report/binary nil)}
          "clear"]]]]]]))

(defn row
  [movie]
  (into ^{:key (:db/id movie)} [:tr]
        (map (fn [field] [:td (format-cell (field movie))]) fields)))

(defn app
  []
  (let [movies @(rf/subscribe [::ds/filtered-movies])]
    [:div 
     [report-form]
     [:div (str "showing " (count movies) " movies")]
     [:table
      [:thead
       [:tr (map (fn [field] ^{:key field} [:th (name field)]) fields)]]
      [:tbody (map row movies)]]]))
