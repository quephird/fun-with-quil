(ns fun-with-quil.sketches.neato
  (:use quil.core))

(def screen-w 1024)
(def screen-h screen-w)

(def k (atom 0.0))

(defn normalize [k]
  (let [l (mod k 512)]
    (if (> l 255) (- 511 l) l)))

(defn get-color-component []
  (let [dk (rand)]
    (swap! k + dk)
    (normalize @k)))

(defn setup []
  (smooth)
  (no-loop))

(defn draw []
    (doseq [x (range screen-w)
            y (range screen-h)]
  (let [c (repeatedly 3 (fn [] (get-color-component)))]
      (apply stroke c)
      (stroke 0 (get-color-component) 0)
      (point x y)))
  (save "neato.png"))

(sketch
  :title "neato"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
