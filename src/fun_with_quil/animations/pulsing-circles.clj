(ns fun-with-quil.animations.pulsing-circles
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn init-pulsars []
  (into {}
    (for [x (range 180)]
      [x [(q/random 180) (q/random 255)]])))

(defn setup []
  (q/no-stroke)
  (q/smooth)
  (q/ellipse-mode :center)
  (q/color-mode :hsb)
  (init-pulsars))

(defn draw [pulsars]
  (let [fc   (q/frame-count)
        w    (q/width)
        cols 18
        D    (/ w cols)]
    (q/background 0)

    (q/translate (* D 0.5) (* D 0.5))

    ; TODO: Change for three pulsating concentric rings
    (doseq [[i [dθ h]] pulsars]
      (let [row (quot i cols)
            col (rem i cols)
            d (+ 40 (* 40 (q/sin (* 3 (q/radians (+ fc dθ))))))
            b (+ 127 (* 127 (q/cos (* 3 (q/radians (+ fc dθ))))))]
        (q/push-matrix)
        (q/translate (* col D) (* row D))
        (q/fill h 255 b)
        (q/ellipse 0 0 d d)
        (q/pop-matrix)))))

(q/defsketch pulsing-circles
  :title "pulsing circles"
  :setup setup
  :size [1440 800]
  :draw draw
  :middleware [m/fun-mode])
