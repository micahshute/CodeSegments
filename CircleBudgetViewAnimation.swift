//
//  BudgetView.swift
//  Venture
//
//  Created by Micah Shute on 8/11/17.
//  Copyright © 2017 shuteApps. All rights reserved.
//

import UIKit

//@IBDesignable
class BudgetView: UIView {
    
    //@IBInspectable
    var alphaForView: CGFloat = 0.35
    
    //@IBInspectable
    var remainingColor: UIColor = UIColor.green
    
    //@IBInspectable
    var usedColor: UIColor = UIColor.red
    
    //@IBInspectable
    var lineWidth: CGFloat = 20.0
    
    //@IBInspectable
    var fraction: CGFloat{
        
        get{
            return CGFloat(_fraction)
        }
        
        set{
            _fraction = (newValue > 1.0 || newValue < 0) ? 1.0 : newValue
            setNeedsDisplay()
        }
    }
    
    //@IBInspectable
    var scale: CGFloat = 0.9
    
    
    private var _fraction: CGFloat = 0.7
    
    private var circleRadius: CGFloat{
        get{
            return min(bounds.size.width , bounds.size.height ) / 2 * scale
        }
    }
    
    private var circleCenter: CGPoint {
        get{
            return CGPoint(x: bounds.midX, y: bounds.midY)
        }
    }
    
    private func pathForMoneyRemaining(_ fraction: CGFloat) -> UIBezierPath{
        let path = UIBezierPath(arcCenter: circleCenter, radius: circleRadius, startAngle: -1 * CGFloat.pi * (1/2), endAngle: (fraction == 0) ? (-1 * CGFloat.pi * (1/2) - (2 * CGFloat.pi)) :(2 * CGFloat.pi * fraction) - (0.5 * CGFloat.pi), clockwise: false)
        path.lineWidth = lineWidth
        return path
    }
    
    private func pathForMoneyUsed(_ fraction: CGFloat) -> UIBezierPath{
        let path = UIBezierPath(arcCenter: circleCenter, radius: circleRadius, startAngle: -1 * CGFloat.pi * (1/2), endAngle: (2 * CGFloat.pi * fraction) - (0.5 * CGFloat.pi), clockwise: true)
        path.lineWidth = lineWidth
        return path
    }
    
//    func animate(with newFraction: CGFloat){
//        let oldPathRemaining = pathForMoneyRemaining(0)
//        let oldPathUsed = pathForMoneyUsed(0)
//        let newPathRemaining = pathForMoneyRemaining(1)
//        let newPathUsed = pathForMoneyUsed(1)
//        remainingColor.set()
//        newPathRemaining.stroke(with: .normal, alpha: alphaForView)
//        usedColor.set()
//        newPathUsed.stroke(with: .normal, alpha: alphaForView)
//
//    }
//    
    override func draw(_ rect: CGRect) {
        
        
        let pathForRemaining = pathForMoneyRemaining(_fraction)
        let pathForUsed = self.pathForMoneyUsed(_fraction)

        remainingColor.set()
        pathForRemaining.stroke(with: .normal, alpha: alphaForView)
        usedColor.set()
        pathForUsed.stroke(with: .normal, alpha: alphaForView)

        

        
        
    }
    

}
