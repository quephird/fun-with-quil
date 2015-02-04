(ns fun-with-quil.animations.spinny-emoticon
  (:require [quil.core :as q :include-macros true]))

(defn setup []
  (q/smooth)
  (q/frame-rate 3)
  (q/text-font (q/create-font "Courier" 48))
;  (q/no-loop)
  )

(defn draw []
  (let [fc (q/frame-count)
        w  (q/width)
        h  (q/height)
        emoticon-frames ["●ヽ( ･ω･`)ﾉ●"
                         " ●ヽ(･ω･`)ﾉ●"
                         "  ●(ω･`ﾉ●"
                         "   (･`● )"
                         " (●　　)ﾉ●"
                         "●ヽ(　　 )ﾉ●"
                         " ●(　 ´)ﾉ●"
                         "  (　´ﾉ●"
                         "  ( ﾉ● )"
                         "   ●´･ω)"
                         "●ヽ( ･ω･)●"]]
    (q/background 255)
;    (q/text-size 18)
    (q/translate 100 250)
    (q/fill 0)
    (q/text (get emoticon-frames (mod fc (count emoticon-frames))) 0 0)
    )
  )

(q/defsketch spinny-emoticon
  :title "spinny-emoticon"
  :setup setup
  :draw draw
  :size [500 500])
