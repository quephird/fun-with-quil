(ns fun-with-quil.animations.pinwheel
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-triangle []
  {:r 0
   :θ (q/random 360)
   :h (q/random 50)})

(defn setup []
  (q/smooth)
  (q/no-stroke)
  (q/color-mode :hsb)
  [(make-triangle)])

(defn update [triangles]
  (let [new-triangle (if (< (q/random 1) 0.1) [(make-triangle)] nil)]
    (->> triangles
      (filter (fn [{r :r}] (< r 400)))
      (map (fn [t] (update-in t [:r] + 2)))
      (map (fn [t] (update-in t [:θ] + 5)))
      (concat new-triangle))))

(defn draw [triangles]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)]
    (q/fill 0 10)
    (q/rect 0 0 w h)
    (q/translate (* 0.5 w) (* 0.5 h))
    (doseq [{:keys [r θ h]} triangles]
      (let [x1 (* r (q/cos (q/radians θ)))
            y1 (* r (q/sin (q/radians θ)))
            x2 (* 1.5 x1)
            y2 (* 1.5 y1)
            x3 (* 1.25 r (q/cos (q/radians (+ θ 20))))
            y3 (* 1.25 r (q/sin (q/radians (+ θ 20))))]
        (q/fill h 255 255)
        (q/begin-shape :triangles)
        (q/vertex x1 y1)
        (q/vertex x2 y2)
        (q/vertex x3 y3)
        (q/end-shape)))))

(q/defsketch pinwheel
  :size   [800 800]
  :title  "pinwheel"
  :setup  setup
  :update update
  :draw   draw
  :middleware [m/fun-mode])
