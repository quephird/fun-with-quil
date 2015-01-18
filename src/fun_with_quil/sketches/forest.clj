(ns fun-with-quil.sketches.forest
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(defn make-forest [xys cs]
  (for [[x y] xys]
    {:c (cs (rand-int (count cs)))
     :x (+ x (q/random -100 100))
     :y (+ y (q/random -100 100))
     :z 0
     :θ (q/random q/TWO-PI)}))

(defn setup []
  (let [xys [[-3000 -1500]
               [-2000 -1000]
               [-2000 -2000]
               [-1000 -500]
               [-1000 -1500]
               [-1000 -2500]
               [0 0]
               [0 -1000]
               [0 -2000]
               [0 -3000]
               [1000 -500]
               [1000 -1500]
               [1000 -2500]
               [2000 -1000]
               [2000 -2000]
               [3000 -1500]]
        cs [[252 56 70]
            [252 114 76]
            [157 63 83]
            [252 177 70]
            [252 152 31]
            [219 42 80]
            [233 52 121]
            [239 50 59]]]
    (q/no-stroke)
    (q/no-loop)
    (q/ellipse-mode :center)
    (make-forest xys cs)))

(defn cone [{:keys [x y]}]
  (let [r 100
        h 2000
        n 40]
    (q/push-matrix)
    (q/translate x y h)
    (let [t (/ q/TWO-PI n)]
      (q/fill 63)
      (dotimes [i n]
        (q/begin-shape :triangles)
        (q/vertex r 0 0)
        (q/vertex (* r (q/cos t)) (* r (q/sin t)) 0)
        (q/vertex 0 0 (- h))
        (q/end-shape)
        (q/rotate (- t))))
     (q/pop-matrix)))

(defn upright-fn [t p]
  [(* (+ (* t 10) (* 12 (q/sqrt t) (q/cos p))) (q/cos t))
   (* (+ (* t 10) (* 12 (q/sqrt t) (q/cos p))) (q/sin t))
   (+ (* 50 t) (* 12 (q/sqrt t) (q/sin p)))])

(defn reflected-fn [t p]
  [(* (+ (* t 10) (* 12 (q/sqrt t) (q/cos p))) (q/cos t))
   (* (+ (* t 10) (* 12 (q/sqrt t) (q/cos p))) (q/sin t))
   (+ (* -50 t) (* 12 (q/sqrt t) (q/sin p)))])

(defn spiral [{:keys [x y z c θ]} f]
  (q/push-matrix)
  (q/translate x y z)
  (q/rotate-z θ)
  (apply q/fill c)
  (let [dt (/ q/TWO-PI 100)
        dp (/ q/TWO-PI 40)]
    (doseq [t (range 0 (* 6 q/TWO-PI) dt)]
      (doseq [p (range 0 q/TWO-PI dp)]
        (q/begin-shape :quads)
        (doseq [angle [[t p]
                       [(+ t dt) p]
                       [(+ t dt) (+ p dp)]
                       [t (+ p dp)]]]
          (apply q/vertex (apply f angle)))
        (q/end-shape))))
  (q/pop-matrix))

(defn upright [t]
  (spiral t upright-fn))

(defn reflected [t]
  (let [t' (-> t
             (update-in [:z] + 4000)
             (update-in [:c] (fn [c] (map #(* 0.25 %) c))))]
    (spiral t' reflected-fn)))

(defn reflected-cone [t]
  (q/push-matrix)
  (q/rotate-y q/PI)
  (q/translate -100 -100 -4000)
  (cone t)
  (q/pop-matrix))

(defn draw-tree [t]
  (upright t)
  (cone t)
  (reflected t)
  (reflected-cone t)
  )

(defn draw [forest]
  (let [w  (q/width)
        h  (q/height)]
    (q/background 0)
    (q/translate (* 0.5 w) (* 0.5 h))

    (q/camera 0 2000 -400
              0 0 750
              0 0 1)
    (q/point-light 168 168 168
                   0 2000 -1000)

    (doseq [tree forest]
      (draw-tree tree))
    (q/save "forest.png")))

(q/defsketch forest
  :title      "forest"
  :size       [1440 800]
  :renderer   :p3d
  :setup      setup
  :draw       draw
  :middleware [m/fun-mode])
