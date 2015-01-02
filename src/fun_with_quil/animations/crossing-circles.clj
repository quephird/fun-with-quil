(ns fun-with-quil.animations.crossing-circles
  (:require [quil.core :as q :include-macros :true]))

(defn circles [sw]
  (q/stroke-weight sw)
  (doseq [i (range 1 15)]
    (let [dr (* sw 2.5)
          r (* i dr)]
      (q/ellipse 0 0 r r))))

(defn setup []
  (q/stroke 0)
  (q/ellipse-mode :center)
  (q/no-fill))

(defn draw []
  (let [θ         (* 1 (q/frame-count))
        w         (q/width)
        h         (q/height)
        amplitude (* 0.5 w)
        phase     (* 0.5 w)]
    (q/background 255)
    (doseq [x [(+ phase (* amplitude (q/sin (q/radians θ))))
               (+ phase (* (- amplitude) (q/sin (q/radians θ))))]]
      (q/push-matrix)
      (q/translate x (* 0.5 h))
      (circles 20)
      (q/pop-matrix))))

(q/sketch
  :title "crossing-circles"
  :setup setup
  :draw draw
  :size [800 800])
