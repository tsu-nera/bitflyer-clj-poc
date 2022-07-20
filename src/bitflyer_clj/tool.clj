(ns bitflyer-clj.tool
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io])
  (:import
   (javax.crypto
    Mac)
   (javax.crypto.spec
    SecretKeySpec)))

(defn load-edn [file-path]
  (when-let [file (io/resource file-path)]
    (-> file
        slurp
        edn/read-string)))

(defn secretKeyInst [key mac]
  (SecretKeySpec. (.getBytes key) (.getAlgorithm mac)))

(defn toHexString
  "Convert bytes to a String"
  [bytes]
  (apply str (map #(format "%x" %) bytes)))

(defn sign
  "Returns the signature of a string with a given
  key, using a SHA-256 HMAC."
  [key text]
  (let [mac       (Mac/getInstance "HMACSHA256")
        secretKey (secretKeyInst key mac)]
    (-> (doto mac
          (.init secretKey)
          (.update (.getBytes text)))
        .doFinal
        toHexString)))
