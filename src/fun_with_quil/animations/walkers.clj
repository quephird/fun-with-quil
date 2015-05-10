(ns fun-with-quil.animations.walkers
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-walker []
  {:r 0
   :θ (q/random 360)
   :h (q/random 255)})

(defn setup []
  (q/color-mode :hsb)
  (q/ellipse-mode :center)
  [])

(defn update [walkers]
  (let [fc (q/frame-count)]
    (->> walkers
      ; Move each walker out.
      (map (fn [w] (update-in w [:r] + 5)))
      ; Add a new walker only each third frame.
      ((fn [walkers] (if (zero? (mod fc 3)) (cons (make-walker) walkers) walkers)))
      ; Remove walkers that are outside the frame.
      (filter (fn [{r :r}] (< r 500))))))

(defn draw [walkers]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        cell-w (/ w 15)]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))
    (doseq [{:keys [r θ h]} walkers]
      (let [h' (mod (+ h (* 2 fc)) 255)
            ; Coords of center of each walker.
            x  (* r (q/cos (q/radians θ)))
            y  (* r (q/sin (q/radians θ)))
            ; Compute xs and ys for all four corners.
            x1 (* cell-w  (quot x cell-w ))
            y1 (* cell-w  (quot y cell-w ))
            x2 (* cell-w  (inc (quot x cell-w )))
            y2 (* cell-w  (inc (quot y cell-w )))]
        (q/no-stroke)
        (q/fill h' 255 255)
        (q/ellipse x y 10 10)
        (q/stroke h' 255 255)
        (doseq [[x' y'] [[x1 y1] [x2 y1] [x1 y2] [x2 y2]]]
          ; Subtract half the cell width to put the origin in the middle of a cell.
          (q/line x y (- x' (* 0.5 cell-w)) (- y' (* 0.5 cell-w))))))))

(q/defsketch walkers
  :size       [800 800]
  :title      "walkers"
  :setup      setup
  :update     update
  :draw       draw
  :middleware [m/fun-mode])
