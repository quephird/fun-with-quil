(ns fun-with-quil.animations.spinny-rings
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def screen-w 800)
(def screen-h 800)

(defn setup []
  (q/smooth)
  (q/no-fill)
  (q/ellipse-mode :center)
  (q/background 0)
  (q/stroke 255)
  (q/stroke-weight 3))

(defn dashed-circle [x y r]
  (let [dash-l  20
        n       (/ (* q/TWO-PI r) 2 dash-l)
        dθ      (/ q/TWO-PI 2 n)]
    (doseq [i (range n)]
      (let [θ (/ (* q/TWO-PI i) n)]
        (q/arc x y r r θ (+ θ dθ))))))

(defn rings []
  (doseq [i (range 1 11)]
    (q/push-matrix)
    (q/rotate-x 0.75)
    (q/rotate-z (- 0 0.5 (* 0.1 i)))
    (q/rotate-y (* 0.02 (q/frame-count) (- 11 i)))
    (if (odd? i)
      (q/ellipse 0 0 (* i 50) (* i 50)))
      (dashed-circle 0 0 (* i 50))
    (q/pop-matrix)))

(defn draw [state]
  (q/clear)
  (q/no-fill)
  (q/with-translation [400 400]
    (solid-rings)))

(q/sketch
  :title "Spinny rings"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [screen-w screen-h]
  :middleware [m/fun-mode])
