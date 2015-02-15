(ns fun-with-quil.animations.o
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/no-stroke)
  (q/color-mode :hsb)
  )

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        n  15]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))

    (doseq [i (range n 0 -1)]
      (let [dθ (q/map-range i 0 n 0 (q/radians 120))
            θ  (q/map-range (q/sin (+ dθ (* 0.05 fc))) -1 1 (q/radians -70) (q/radians 70))
            s  (+ 50 (* 30 i))
            c  (q/map-range i 0 (dec n) 255 0)
            ds (q/sqrt (* 2500 i))]
        (q/push-matrix)
        (q/rotate θ)
        (q/fill c)
        (q/begin-shape)

        (q/vertex s 0)
        (q/vertex (- s 20) -15)
        (q/bezier-vertex (- s ds) (* -1.0 s)
                         (- ds s) (* -1.0 s)
                         (- 20 s) -15)
        (q/vertex (- 20 s) -15)

        (q/vertex (- s) 0)
        (q/vertex (- 20 s) 15)
        (q/bezier-vertex (- ds s) (* 1.0 s)
                         (- s ds) (* 1.0 s)
                         (- s 20) 15)
        (q/end-shape :close)
        (q/pop-matrix)))))

(q/defsketch o
  :title "o"
  :setup setup
  :draw draw
  :size [800 800])
