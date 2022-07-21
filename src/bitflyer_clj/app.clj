(ns bitflyer-clj.app
  (:require
   [bitflyer-clj.api :as api]
   [chime.core :as chime])
  (:import
   (java.time
    Duration
    Instant)))

(def app (atom nil))

(defn start []
  (println "Bot started.")
  (deref @app))

(defn stop []
  (.close @@app))

(defn exec [time]
  (println (api/fetch-tick)))

(defn- make-interval-seq [times interval]
  (let [now (Instant/now)]
    (->> (range 1 (+ 1 times))
         (map #(* interval %))
         (map (fn [x] (.plusSeconds now x)))
         (into []))))

(defn- make-periodic-seq [interval]
  (chime/periodic-seq
   (Instant/now)
   (Duration/ofSeconds interval)))

(defn init []
  (reset! app
          (delay (chime/chime-at
                  (make-periodic-seq 3)
                  exec
                  {:on-finished
                   (fn []
                     (println "Bot finished."))}))))

(comment
  (init)
  (start)
  (stop)
  )

(comment
  (def state
    (atom (-> (chime/periodic-seq
               (Instant/now)
               (Duration/ofSeconds 3))
              exec)))

  (.close @state)

  )
