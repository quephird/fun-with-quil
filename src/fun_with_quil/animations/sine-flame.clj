(ns fun-with-quil.animations.sine-flame
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn flame [x dy h dt]
  (q/fill h 255 255)
  (q/push-matrix)
  (q/translate x 100)
  (q/rotate q/PI)
  (q/begin-shape)
  (doseq [i (range 50 (+ 7 dy) -1)]
    (q/vertex (* 200 (q/exp (* -0.1 i)) (q/sin (q/radians (+ dt (* 8 (q/frame-count)) (* 3 q/TWO-PI i))))) (* -10 i)))
  (doseq [i (range (+ 7 dy) 50)]
    (q/vertex (* 200 (q/exp (* -0.1 i)) (q/cos (q/radians (+ dt (* 8 (q/frame-count)) (* 1 q/TWO-PI i))))) (* -10 i)))
  (q/end-shape :close)
  (q/pop-matrix))

(defn setup []
  (q/smooth)
  (q/color-mode :hsb)
  (q/no-stroke)
  (repeatedly 100 (fn [] [(q/random 800) (q/random 0 30) (q/random 40) (rand-int 180)])))

(defn draw [state]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (q/background 0)
    (doseq [[x dy h dt] state]
      (flame x dy h dt))))

(q/defsketch sine-flame
  :title "sine-flame"
  :setup setup
  :draw draw
  :size [800 800]
  :middleware [m/fun-mode])
