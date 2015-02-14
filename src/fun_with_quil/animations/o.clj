(ns fun-with-quil.animations.o
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/no-stroke)
  )

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        n  12]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))

    (doseq [i (range n 0 -1)]
      (let [dθ (q/map-range i 0 n 0 (q/radians 120))
            θ  (q/map-range (q/sin (+ dθ (* 0.05 fc))) -1 1 (q/radians -70) (q/radians 70))
            s  (+ 50 (* 30 i))
            c  (q/map-range i 0 (dec n) 255 0)
            ]
        (q/push-matrix)
        (q/rotate θ)
        (q/fill c)
        (q/begin-shape)
        (q/vertex s 0)
        (q/vertex (* 0.5 s) (* -0.3 s))
        (q/bezier-vertex (* 0.3 s) (* -0.6 s)
                         (* -0.3 s) (* -0.6 s)
                         (* -0.5 s) (* -0.3 s))
        (q/vertex (* -0.5 s) (* -0.3 s))
        (q/vertex (- s) 0)
        (q/vertex (* -0.5 s) (* 0.3 s))
        (q/bezier-vertex (* -0.3 s) (* 0.6 s)
                         (* 0.3 s) (* 0.6 s)
                         (* 0.5 s) (* 0.3 s))
        (q/end-shape :close)
                (q/pop-matrix)
))
    )
  )

(q/defsketch o
  :title "o"
  :setup setup
  :draw draw
  :size [800 800])
