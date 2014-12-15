(ns fun-with-quil.animations.pulsating-flowers
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-flowers [n max-x max-y min-h max-h]
  (into []
    (for [f (range n)]
      [(rand-int max-x)
       (rand-int max-y)
       (+ min-h (rand-int (- max-h min-h)))])))

(defn setup []
  (let [w (q/width)
        h (q/height)]
    (q/smooth)
    (q/color-mode :hsb)
    (q/no-stroke)
    (make-flowers 15 w h 0 40)))

(defn update [flowers]
  flowers)

(defn draw [flowers]
  (let [petal-count 15
        flower-r    100
        petal-w     (* 2 flower-r)
        t           (* 20 (q/frame-count))]
    (q/background 0)
    (doseq [[flower-x flower-y h] flowers]
      (q/push-matrix)
      (q/translate flower-x flower-y)
      (q/fill h 127 255 50)
      (doseq [i (range petal-count)]
        (let [θ (q/radians (* i (/ 360 petal-count)))
              a (* flower-r 0.25)
              dr (+ a (* a (q/sin (q/radians t))))
              x (* (- flower-r dr) (q/cos θ))
              y (* (- flower-r dr) (q/sin θ))]
          (q/ellipse x y petal-w petal-w)))
      (q/pop-matrix))))

(sketch
  :title      "pulsating flowers"
  :size       [1400 800]
  :setup      setup
  :draw       draw
  :update     update
  :middleware [m/fun-mode])
