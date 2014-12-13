(ns fun-with-quil.sketches.polar-vortex
  (:use quil.core))

(def screen-w 1200)
(def screen-h 800)

(defn setup []
  (background 0)
  (smooth)
  (ellipse-mode :center)
  (no-loop))

(defn draw []
  (let [base-r 200
        base-d (* base-r 2)]
    (translate (* base-r 1.25) (* base-r 1.25))
    (doseq [i (range 400)]
      (let [b (+ 55 (rand-int 200))
            g (/ b 2)
            d (* base-d (Math/exp (* i -0.01)))]
        (fill 0 g b)
        (ellipse 0 0 d d)
        )
      (translate 3 1))))

(sketch
  :title "polar-vortex"
  :setup setup
  :draw draw
  :size [screen-w screen-h]
  :renderer :java2d)
