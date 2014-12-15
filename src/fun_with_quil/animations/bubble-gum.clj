(ns quil.animations.bubble-gum
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-piece [max-x max-y max-w colors]
  {:x   (q/random max-x)                       ; Absolute x for center of revolution
   :y   (q/random max-y)                       ; Absolute y for center of revolution
   :r   (+ max-w (q/random (/ max-w 2)))       ; Radius of revolution
   :w   (+ (/ max-w 2) (q/random (/ max-w 2))) ; Average width of piece
   :θ   (q/random 360)                         ; Angle of revolution
   :dθ  (q/random 1 3)                         ; Change in angle of revolution
   :ø   (q/random 90)                          ; Angle of rotation
   :dø  (q/random 2 5)                         ; Change in angle of rotation
   :c   (colors (rand-int (count colors)))})   ; Color of piece

(defn init-pieces [max-x max-y]
  (let [piece-count 150
        max-w 75
        colors [[255 0 0]
                [255 127 0]
                [255 255 0]
                [0 255 0]
                [0 0 255]]]
    (into []
      (for [i (range piece-count)]
        (make-piece max-x max-y max-w colors)))))

(defn setup []
  (let [w (q/width)
        h (q/height)]
    (q/smooth)
    (q/no-stroke)
    (init-pieces w h)))

(defn update [pieces]
  (->> pieces
    (map (fn [{dθ :dθ :as piece}] (update-in piece [:θ] + dθ)))
    (map (fn [{dø :dø :as piece}] (update-in piece [:ø] + dø)))))

(defn draw [pieces]
  (q/background 0)
  (doseq [{:keys [x y r w θ dθ ø dø c]} pieces]
    (let [actual-x (+ x (* r (q/cos (q/radians θ))))
          actual-y (+ y (* r (q/sin (q/radians θ))))
          actual-w (+ w (* w 0.5 (q/sin (q/radians ø))))]
      (q/push-matrix)
      (apply q/fill c)
      (q/translate actual-x actual-y)
      (q/rotate (q/radians ø))
      (q/rect (* actual-w -0.5) (* actual-w -0.5) actual-w actual-w (* actual-w 0.25)))
      (q/pop-matrix)))

(q/sketch
  :title      "bubble gum"
  :size       [800 800]
  :setup      setup
  :update     update
  :draw       draw
  :middleware [m/fun-mode])
