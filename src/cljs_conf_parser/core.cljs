(ns cljs-conf-parser.core
	(:require [reagent.core :as reagent]
          [cljs.reader :refer [read-string]]
          [re-frame.core :refer [register-handler
                                   path
                                   debug
                                   register-sub
                                   dispatch
                                   dispatch-sync
                                   subscribe]]
 		  [instaparse.core :as insta]
 		  [fipp.clojure :as f]
 		  [cljs-conf-parser.init :as init]
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


(defn get-input     
   [db _] 
   (reaction (:input @db))) 

(register-sub
	:input
	get-input)

(defn get-grammar     
   [db _]   
   (reaction (:grammar @db))) 

(register-sub
	:grammar
	get-grammar)

(defn get-result    
   [db _]  
   (reaction (:result @db)))

(register-sub
	:result
	get-result)


(register-handler
  :submit
  (fn
    [db _]
    (let [nh {:input (str (.-value (js/document.getElementById "input")))
    	        :grammar (str (.-value (js/document.getElementById "grammar")))}]
    (merge db nh)
    )))

(defn parser
	[grammar input]
	(print "in parser")
	(let [parser   (insta/parser grammar)
		    result   (parser input)]
		    (if (insta/failure? result)
		 	      (insta/get-failure result)
		 	      result)))

(register-handler
	:parser-generator
	(fn
	  [db _]
	  (let [i      (:input db)
	  	    g      (:grammar db)
	  	    result (parser g i)
	  	   ]
	  	   (assoc db :result result)
	  	    )))

(defn generate-result
	[input]
 	(dispatch [:submit input])
 	(dispatch [:parser-generator]))

(defn header-template
      [title items]
        [:nav.brocade-red {:role "navigation"}
                  [:div.nav-wrapper.container
                   [:a.brand-logo {:href "" :id "logo-container"} [:h1.brocade-logo] [:span.sub-title title]]
                   [:ul.right.hide-on-med-and-down 
                    (map
                      (fn [{:keys [title href]}]
                         ^{:key title} [:li [:a {:href href} title]])
                     items)]
                   [:ul.side-nav {:id "nav-mobile"} 
                    (map
                      (fn [{:keys [title href]}]
                         ^{:key title} [:li [:a {:href href} title]])
                     items)]
                   [:a.button-collapse {:data-activates "nav-mobile"} [:i.material-icons "menu"]]
                  ]
                ])

(defn main-template
      []
      (fn
      	[]
      	(let [input (atom)
      	      out (subscribe [:result])]
	        [:div.container
	        [:div.row
	             [:form.col.s12
		             	[:div.input-field.col.s6
		             	[:textarea.materialize-textarea {:id "input"}]
		             	[:label {:for "input"} "Input Configuration"]]
		             	[:div.input-field.col.s6
		             	[:textarea.materialize-textarea {:id "grammar" }]
		             	[:label {:for "grammar"} "Input Grammar"]]
	             	]
	        ]
	        [:div.row
	                [:div.col.s12
	             	[:pre  (pr-str @out)]
	             	[:label {:for "output"} "Output Configuration"]
	             	]
	        ]
	        [:div.row
	             	[:button.btn.waves-effect.waves.light {:type "button" :on-click #(generate-result input)} "Submit"]
	        ]
	        ]
        )))




; (defn display-results
; 	[]
; 	(let [f (subscribe [:input])
; 		  g (subscribe [:grammar])]
; 		 (fn 
; 		 	[]
; 			[:div.container
; 			[:textarea.materialize-textarea {:value @f}]
; 			[:textarea.materialize-textarea {:value @g}]
; 			[:label "Test"]
; 			]
; 	)))

(defn Page
  []
  (let [{:keys [app/title app/header app/footer]} page-state]
         [:div
          [header-template title header]
          [main-template]
          ; [display-results]
          ]))


(defn ^:export init
  []
  ; (dispatch-sync [:initialize])
  (reagent/render [Page]
                  (js/document.getElementById "app")))