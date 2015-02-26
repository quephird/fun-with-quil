(ns fun-with-quil.sketches.dandelion
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-dandelion [r]
    (for [ϕ (range 0 181 30) θ (range 0 360 (/ 360 (+ 1 (* 11 (q/sin (q/radians ϕ))))))]
        (let [x (+ (* r (q/cos (q/radians θ)) (q/sin (q/radians ϕ))) (q/random -50 50))
              y (+ (* r (q/sin (q/radians θ)) (q/sin (q/radians ϕ))) (q/random -50 50))
              z (+ (* r (q/cos (q/radians ϕ))) (q/random -10 10))]
          {:x x :y y :z z :dz 0 :θ θ :ϕ ϕ})))

(defn setup []
;  (q/no-loop)
  (make-dandelion 800)
  )

(defn update [dandelion]
  (let [fc (q/frame-count)]
    (->> dandelion
      (map (fn [seed] (assoc-in seed [:dz] (q/map-range (q/sin (* 0.1 fc)) -1 1 10 60)))))))

(defn draw [dandelion]
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        camera-x (* 2000 (q/cos (* 0.01 fc)))
        camera-y (* 2000 (q/sin (* 0.01 fc)))
        camera-z 400
        r  800]
    (q/background 0)
    (q/camera camera-x camera-y camera-z
              0 0 0
              0 0 -1)

    (doseq [{:keys [x y z dz θ ϕ]} dandelion]
          (q/stroke-weight 3)
          (q/stroke 90)
          (q/line 0 0 0 x y z)
          (q/push-matrix)
          (q/rotate-z (q/radians θ))
          (q/rotate-y (q/radians ϕ))
          (q/translate 0 0 r)
          (q/stroke-weight 1)
          (q/stroke 255)
          (doseq [φ (range 0 360 20)]
            (q/line 0 0 0
                    (* 250 (q/cos (q/radians φ))) (* 250 (q/sin (q/radians φ))) dz))
          (q/pop-matrix)
      )
    )
  )

(q/defsketch dandelion
  :size [800 800]
  :title "dandelion"
  :setup setup
  :update update
  :draw draw
  :renderer :p3d
  :middleware [m/fun-mode]
  )
