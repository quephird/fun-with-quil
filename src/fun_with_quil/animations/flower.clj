(ns fun-with-quil.animations.flower
  (:use quil.core))

(def screen-w 800)
(def screen-h 800)

(defn setup []
  (smooth)
  (no-stroke)
  (blend-mode :exclusion)
  (ellipse-mode :center)
  (color-mode :hsb))

(defn draw []
  (let [fc      (frame-count)
        r       (+ 150 (* 150 (sin (/ (* fc PI) 70))))
        θ       (radians (/ (* 40 PI fc) 180))
        h       (map-range (mod (* 0.5 fc) 255) 0 255 0 255)
        n       20
        dθ      (radians (/ 360 n))]
    (background 0)
    (translate (/ screen-w 2) (/ screen-h 2))
    (rotate θ)
    (doseq [i (range n)]
      (fill h 255 255)
      (ellipse 0 250 r r)
      (rotate dθ))))

(sketch
  :title "flower"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [screen-w screen-h])
