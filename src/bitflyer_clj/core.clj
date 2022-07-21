(ns bitflyer-clj.core
  (:require
   [bitflyer-clj.app :as app]
   [chime.core :as chime])
  (:import
   (java.time
    Duration
    Instant)))

;; 実効スプレッド(100%=1,1%=0.01)この値を上回ったらentry
(def spread-entry-th 0.0005)
;; 実効スプレッド (100%=1,1%=0.01) がこの値を下回ったら指値更新を停止
(def spread-cancel-th 0.0003)
;;  実効Ask/BidからDELTA離れた位置に指値をおく.
(def delta 1)

(def position (atom nil))

(def app (atom nil))

(def spread-entry 0.0005)
(def spread-cancel 0.003)

(defn start []
  (println "Bot started.")
  (deref @app))

(defn stop []
  (.close @@app))

(defn- make-periodic-seq [interval]
  (chime/periodic-seq
   (Instant/now)
   (Duration/ofSeconds interval)))

(defn init []
  (reset! app
          (delay (chime/chime-at
                  (make-periodic-seq 3)
                  app/exec
                  {:on-finished
                   (fn []
                     (println "Bot finished."))}))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(comment
  (init)
  (start)
  (stop)
  )

(comment

  ;; 有限のスケジュール.いったん廃止.
  (defn- make-interval-seq [times interval]
    (let [now (Instant/now)]
      (->> (range 1 (+ 1 times))
           (map #(* interval %))
           (map (fn [x] (.plusSeconds now x)))
           (into []))))
  )
