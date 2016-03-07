(ns cljs-conf-parser.core
	(:require [reagent.core :as reagent]
          [cljs.reader :refer [read-string]]
          [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   dispatch-sync
                                   subscribe]]
 		  [instaparse.core :as insta]
          [cljs.core.async :refer [put! chan <! >! close!]]
          )
 (:require-macros [reagent.ratom :refer [reaction]]
 									[cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(print "Welcome to CLJS parser")


(def page-state
  {:app/title "vTM Translator"
   :app/header
              [{:title "Brocade" :href "http://www.brocade.com"}
              ]
   :app/footer
              [{:heading "Projects" :items [{:title "Repositories" :href "https://www.github.com/brocade"}
                                            {:title "OpenDaylight" :href "https://www.opendaylight.org/"}
                                            {:title "OpenStack" :href "https://www.openstack.org/brocade"}]}
               {:heading "Technology" :items [{:title "SDN Controller and Applications" :href "https://www.brocade.com/en/products-services/software-networking/sdn-controllers-applications.html"}
                                              {:title "Network Function Virtualization" :href "https://www.brocade.com/en/products-services/software-networking/network-functions-virtualization.html"}]}
               {:heading "Resources" :items [{:title "Getting Started" :href "https://community.brocade.com/t5/SDN-NFV/ct-p/SdnNfv"}
                                             {:title "Brocade OpenSource Code" :href "https://www.brocade.com/en/support/support-tools/oscd.html"}
                                             {:title "DevNet" :href "https://community.brocade.com/t5/DevNet/ct-p/APISupport"}]}
               {:heading "Contact" :items [{:title "Contact Us" :href "https://www.brocade.com/en/forms/contact-us.html"}]}]
   }
  )


(defonce app-state (atom {:text "Hello world!"}))


(defn header-template
      [title items]
        [:nav.brocade-red {:role "navigation"}
                  [:div.nav-wrapper.container
                   [:a.brand-logo {:href "" :id "logo-container"} [:h1.brocade-logo] [:span.sub-title title]]
                   [:ul.right.hide-on-med-and-down (map
                     (fn [{:keys [title href]}]
                         ^{:key title} [:li [:a {:href href} title]])
                     items)]
                   [:ul.side-nav {:id "nav-mobile"} (map
                     (fn [{:keys [title href]}]
                         ^{:key title} [:li [:a {:href href} title]])
                     items)]
                   [:a.button-collapse {:data-activates "nav-mobile"} [:i.material-icons "menu"]]
                  ]
                ])


(defn main-template
      []
        [:div.container
         [:div.section
           [:div.row
             [:form.col.s12
             	[:div.input-field.col.s6
             	[:textarea.materialize-textarea {:id "textarea1"} ]
             	[:label {:for "textarea1"} "Input Configuration"]
             	[:button.btn.waves-effect.waves.light {:type "submit" :name "action"} "Submit"]]

                [:div.input-field.col.s6
             	[:textarea.materialize-textarea {:id "textarea2"} ]
             	[:label {:for "textarea2"} "Output Configuration"]
             	]
             	]]]]
        )




(defn Page
  []
  (let [{:keys [app/title app/header app/footer]} page-state]
         [:div
          (header-template title header)
          (main-template)
          ]))


(defn ^:export init
  []
  (reagent/render [Page]
                  (js/document.getElementById "app")))