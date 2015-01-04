(ns fun-with-quil.animations.spinny-rounded-rects
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/rect-mode :center)
  (q/no-stroke))

(defn draw []
  (let [fc (q/frame-count)
        w (q/width)
        h (q/height)]
    (q/background 255)
    (q/translate (* w 0.5) (* h 0.5))
    (doseq [i (range 15 0 -1)]
      (let [w (* i 50)
            r (* w 0.4)
            c (* (- 15 i) (/ 255.0 15.0))
            θ (* 0.2 fc (- 15 i))]
        (q/push-matrix)
        (q/rotate (q/radians θ))
        (q/fill c)
        (q/rect 0 0 w w r)
        (q/pop-matrix)))))

(q/sketch
  :title "spinny rounded rects"
  :setup setup
  :draw draw
  :size [800 800])
