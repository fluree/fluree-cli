(ns fluree.fluree-cli
  (:require [fluree.db.api :as db]
            [clojure.core.async :as async :refer [go <!]])
  (:gen-class))

(defn connect
  "Connect to Fluree ledger"
  []
  (let [conn (db/connect ["localhost"])]
    (let [resp-ch (db/new-ledger-async conn "cli/test")]
      (go (let [resp (<! resp-ch)]
            (println "new-ledger resp:" (pr-str resp)))))
    (println "Sleeping...")
    (Thread/sleep 2000) ; give ledger time to create
    (println "Done.")
    (let [ledger-list-ch (db/ledger-list-async conn)
          _ (go (let [ledger-list (<! ledger-list-ch)]
                  (println "Ledger list:" (pr-str ledger-list))))
          db (db/db conn "cli/test")
          _ (println "Got cli/test db")
          query {:select ["*"] :from "_collection"}
          result-ch (db/query-async db query)
          _ (println "Sent query")]
      (go (let [result (<! result-ch)]
            (println "Query result:" (pr-str result))))
      (Thread/sleep 10000))))

(defn -main
  "Entry point"
  [& args]
  (connect)
  (shutdown-agents)
  (System/exit 0))
