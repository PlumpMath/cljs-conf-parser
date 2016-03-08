(ns cljs-conf-parser.init)


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

(def fail-state
[{:repo
             {:name "Brocade",
              :html_url "https://github.com/brocade",
              :forks 1,
              :description "Brocade GitHub Repository",
              :commiters [],
              :lastcommit "4123372a06aa2d26889f2987303b0634198f5807",
              :date "2013-06-03T18:39:46Z",
              :author "Brocade Communications",
              :email "noreply@brocade.com"}}]
  )



(def init-state
  {:init "server real rs2 10.27.13.11\n port default disable\n port http disable\n port http keepalive\n port http url \"HEAD /\"\n port http server-id 1102\n port http group-id  1 1\n!\nserver real rs3 10.27.13.12\n port default disable\n disable\n port http disable\n port http keepalive\n port http url \"HEAD /\"\n port http server-id 1103\n port http group-id  1 1\n!\nserver virtual vi1 172.27.13.54\n port default disable\n port ssl\n port ssl tcp-only\n no port ssl sticky\n port ssl ssl-proxy sslp1-client sslp2-server\n port ssl cookie-name \"persist\"\n port ssl csw-policy \"csp1\"\n bind ssl rs1 ssl\n!"
  :grammar "  config = (server port*  <term> <ws>)*
  server = servers | status
  <servers> = <'server'> <sp> (real | virtual) <sp> key <sp> value <ws>
  real = <'real'>
  virtual = <'virtual'>
  port =  ports | bind |disable
  (* need to fix this, disable only works in port config)
  <ports> = ['no' <sp>] <'port'> <sp> key | (<sp> value)* <ws>
  bind = 'bind ssl' (<sp> key <sp> value) <ws>
  status = !ports disable | enable
  disable  = <'disable'> <ws>
  enable = <'enable'> <ws>
  sp =  #'[ ]+'
  key = string
  value = string | ip-address | quoted_string | number
  <quoted_string> = #'(\".*\")'
  <string> =  #'[a-zA-Z1-9\\-\\_\\.\"\\/]+'
  <number> = #'[0-9]+'
  <ip-address> = #'\\d+\\.\\d+\\.\\d+\\.\\d+'
  <ws> = #'\\s+'
  indent = #'\\^[ ]'
  term = '!'"}
  )