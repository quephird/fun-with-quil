(ns fun-with-quil.animations.tadpoles
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-tadpole [max-x max-y max-r colors]
  {:x  (q/random max-x)                           ; Absolute x of center of revolution
   :y  (q/random max-y)                           ; Absolute y of center of revolution
   :a  (+ (* max-r 0.5) (q/random (* max-r 0.5))) ; Major axis of ellipse of revolution
   :b  (q/random (/ max-r 2))                     ; Minor axis of ellipse of revolution
   :θ  (q/random 360)                             ; Angle of revolution
   :dθ (q/random 5 7)                             ; Change in angle of revolution
   :ø  (q/random 90)                              ; Angle of ellipse of revolution wrt xy-plane
   :c  (colors (rand-int (count colors)))})       ; Color

(defn make-tadpoles [max-x max-y]
  (let [tadpole-count 50
        max-r 100
        colors [[0 255 255]
                [255 0 255]
                [0 0 255]
                [127 0 255]
                [192 127 192]]]
    (into []
      (for [i (range tadpole-count)]
        (make-tadpole max-x max-y max-r colors)))))

(defn setup []
  (let [w (q/width)
        h (q/height)]
    (q/smooth)
    (q/ellipse-mode :center)
    (q/no-stroke)
    (make-tadpoles w h)))

(defn update [tadpoles]
  ; Increment the angle to move it further along the ellipse
  (->> tadpoles
    (map (fn [{dθ :dθ :as tadpole}] (update-in tadpole [:θ] + dθ)))))

(defn draw [tadpoles]
  (q/background 0)

  ; Draw all tadpoles
  (doseq [{:keys [x y a b θ dθ ø c]} tadpoles]
    (q/push-matrix)
    (apply q/fill c)
    (q/translate x y)
    (q/rotate (q/radians ø))

    ; Draw the tadpole by rendering circles of increasing size along the ellipse of revolution
    ; I'm sure there's a more clever way by drawing a single dynamic shape that changes with θ
    ; but this works. ¯\_(ツ)_/¯
    (doseq [i (range 25)]
      (let [segment-θ (+ θ (* i 4))
            segment-d (* i 2)
            segment-x (* a (q/cos (q/radians segment-θ)))
            segment-y (* b (q/sin (q/radians segment-θ)))]
        (q/push-matrix)
        (q/translate segment-x segment-y)
        (q/ellipse 0 0 segment-d segment-d)
        (q/pop-matrix)))
    (q/pop-matrix)))

(q/sketch
  :title      "tadpoles"
  :size       [1400 800]
  :setup      setup
  :draw       draw
  :update     update
  :middleware [m/fun-mode])
