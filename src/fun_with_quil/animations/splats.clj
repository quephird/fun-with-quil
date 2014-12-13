(ns fun-with-quil.animations.splats
  (:require [quil.core :as q :include-macros true]))

; TODO: Introduce paint streaks
(defn splat [x y d h]
  (let [lobes (+ 5 (rand-int 10))]
    (q/fill h 127 255)
    (q/translate x y)
    (q/ellipse 0 0 d d)
    (q/rotate (q/random q/PI))
    (dotimes [_ lobes]
      (let [dθ 15
            dr (+ (* d 0.3) (q/random (* d 0.3)))
            dx (* 0.5 d (q/sin (q/radians dθ)))
            x1 (- dx)
            y1 (* -0.4 d)
            x2 0
            y2 (- (* -0.4 d) dr)
            x3 dx
            y3 y1]
        (q/begin-shape)
        (q/vertex x1 y1)
        (q/bezier-vertex x1 y1 (+ x2 dx) y2 x2 y2)
        (q/bezier-vertex (- x2 dx) y2 x3 y3 x3 y3)
        (q/end-shape :close)
        (q/rotate (q/random (* 2 (/ q/TWO-PI lobes))))))))

(defn setup []
  (q/no-stroke)
  (q/smooth)
  (q/color-mode :hsb)
  (q/background 0)
  (q/ellipse-mode :center))

; TODO: limit to a palette of 4 or 5 harmonious colors
(defn draw []
  (let [x (q/random (q/width))
        y (q/random (q/height))
        d (+ 25 (q/random 75))
        h (q/random 255)]
    (splat x y d h)

    (if (= (q/frame-count) 600)
      (do
        (q/save "splats.png")
        (q/no-loop)))))

(q/defsketch splats
  :title "splats"
  :setup setup
  :size [1920 1080]
  :draw draw)
