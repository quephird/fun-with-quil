(ns fun-with-quil.animations.sea-breeze
  (:use quil.core))

(def dθ (atom 0))

(defn setup []
  (background 0)
  (ellipse-mode :center)
  (no-stroke)
  (smooth))

(defn draw []
  (let [d 20
        r (/ d 2)
        rows (/ (height) d)
        cols (/ (width) d)]
    (translate r r)
    (doseq [row (range rows) col (range cols)]
      (push-matrix)
      (translate (* col d) (* row d))
      (rotate (radians (* 180 (sin (radians (+ @dθ (* row col)))))))
      (fill 127 255 127)
      (ellipse 0 0 d d)
      (fill 127 127 0)
      (arc 0 0 d d 0 (radians 180))
      (pop-matrix)))
  (swap! dθ + 5))

(sketch
  :title "sea breeze"
  :setup setup
  :draw draw
  :size [800 600])
