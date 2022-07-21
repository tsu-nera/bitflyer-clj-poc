(ns bitflyer-clj.app
  (:require
   [bitflyer-clj.api :as api]
   [chime.core :as chime])
  (:import
   (java.time
    Duration
    Instant)))

(defn start []
  (println "Bot started."))

(defn stop []
  (println "Bot finished."))

(defn exec [time]
  (println (api/fetch-tick)))

(defn- make-interval-seq [times interval]
  (let [now (Instant/now)]
    (->> (range 1 (+ 1 times))
         (map #(* interval %))
         (map (fn [x] (.plusSeconds now x)))
         (into []))))

#_(-> (chime/periodic-seq (Instant/now) (Duration/ofMinutes 5))
      rest)

(defn run []
  (start)
  (let [interval-seq (make-interval-seq 3 2)]
    (chime/chime-at interval-seq
                    exec
                    {:on-finished stop})))
#_(run)
