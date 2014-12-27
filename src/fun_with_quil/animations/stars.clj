(ns fun-with-quil.animations.stars
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-stars [n w h]
  (into []
    (for [i (range n)]
      {:x (q/random w)
       :y (q/random h)
       :r (q/random 1 100)
       :θ (q/random 90)
       :h (q/random 255)})))

(defn setup []
  (let [w  (q/width)
        h  (q/height)]
    (q/no-stroke)
    (q/color-mode :hsb)
    {:w w :h h :stars (make-stars 50 w h)}))

(defn update [{w :w h :h stars :stars :as state}]
  (let [living-stars (filter (fn [{r :r}] (> r 1)) stars)
        new-stars    (make-stars (- (count stars) (count living-stars)) w h)]
    (assoc-in state [:stars]
      (->> living-stars
        (map (fn [star] (-> star
                          (update-in [:θ] + 5)
                          (update-in [:r] - 1))))
        (concat new-stars)))))

(defn draw-star [{:keys [x y r θ h]}]
  (let [half-w (* 0.15 r)]
    (q/push-matrix)
    (q/translate x y)
    (q/rotate θ)
    (q/fill h 255 255)
    (q/begin-shape)
    (q/vertex (- half-w) (- half-w))
    (q/vertex 0 (- r))
    (q/vertex half-w (- half-w))
    (q/vertex r 0)
    (q/vertex half-w half-w)
    (q/vertex 0 r)
    (q/vertex (- half-w) half-w)
    (q/vertex (- r) 0)
    (q/end-shape :close)
    (q/pop-matrix)))

(defn draw [{w :w h :h stars :stars}]
  (q/fill 0 0 0 10)
  (q/rect 0 0 w h)
  (doseq [star stars]
    (draw-star star)))

(q/defsketch stars
  :title "stars"
  :size [1440 800]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])
