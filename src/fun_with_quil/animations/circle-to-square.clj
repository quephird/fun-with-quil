(ns fun-with-quil.animations.circle-to-square
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-cells [m n]
  (into []
    (for [_ (range m)]
      (into []
        (for [_ (range n)]
          {:dθ (q/random 180)})))))

(defn setup []
  (let [rows 12 cols 12]
    (q/stroke-weight 4)
    (q/ellipse-mode :center)
    (q/rect-mode :center)
    (q/color-mode :hsb)
    (q/smooth)
    (make-cells rows cols)))

(defn draw [cells]
  (let [fc   (q/frame-count)
        w    (q/width)
        h    (q/height)
        cell-h (/ h (count cells))
        cell-w (/ w (count (first cells)))
        inner-r (* 0.33 cell-w)]
    (q/background 255)
    (q/translate (* 0.5 cell-w) (* 0.5 cell-h))
    (doseq [row cells]
      (q/push-matrix)
      (doseq [{dθ :dθ} row]
        (let [outer-r (q/map-range (q/sin (* 4 (q/radians (+ fc dθ)))) -1 1 0 (* 0.5 cell-w))
              b       (q/map-range (q/sin (* 4 (q/radians (+ fc dθ)))) -1 1 0 255)]
          (q/fill 255)
          (q/rect 0 0 (* 0.9 cell-w) (* 0.9 cell-h) outer-r)
          (q/fill 255 0 b)
          (q/ellipse 0 0 inner-r inner-r)
          (q/translate 0 cell-w)))
      (q/pop-matrix)
      (q/translate cell-h 0))))

(q/defsketch circle-to-square
  :title      "circle to square... and back again"
  :size       [800 800]
  :setup      setup
  :draw       draw
  :middleware [m/fun-mode])
