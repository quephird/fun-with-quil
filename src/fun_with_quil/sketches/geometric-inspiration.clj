(ns fun-with-quil.sketches.geometric-inspiration
  (:use quil.core))

(def screen-w 1920)
(def screen-h 1080)

(defn setup []
  (background 0)
  (smooth)
  (no-loop))

(defn draw []
  (let [color-cycle [[255 255 255]
                     [70 44 47]
                     [87 20 11]
                     [162 68 42]]]
    (translate 1300 400)
    (doseq [f (range 200)]
      (apply fill (color-cycle (rem (quot f 3) (count color-cycle))))
      (rotate (radians 28))
      (push-matrix)
      (translate 0 (+ 10 (* f f 0.02)))
      (stroke-weight (+ 1 (quot f 12)))
      (rect -2000 0 4000 (+ 20 (* 4 (quot f 12))))
      (pop-matrix)))
  (save "geometric-inspiration.png"))

(sketch
  :title "geometric-inspiration"
  :setup setup
  :draw draw
  :size [screen-w screen-h])
