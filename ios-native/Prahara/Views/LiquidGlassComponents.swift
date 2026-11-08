import SwiftUI

public struct IOSCard<Content: View>: View {
    let content: Content
    var onClick: (() -> Void)? = nil

    public init(onClick: (() -> Void)? = nil, @ViewBuilder content: () -> Content) {
        self.onClick = onClick
        self.content = content()
    }

    public var body: some View {
        Group {
            if let onClick = onClick {
                Button(action: onClick) {
                    cardContent
                }
                .buttonStyle(ScaleButtonStyle())
            } else {
                cardContent
            }
        }
    }

    private var cardContent: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 24, style: .continuous)
                .fill(
                    LinearGradient(
                        colors: [Color.white.opacity(0.08), Color.white.opacity(0.03)],
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    )
                )
                .background(.ultraThinMaterial, in: RoundedRectangle(cornerRadius: 24, style: .continuous))
                .overlay(
                    RoundedRectangle(cornerRadius: 24, style: .continuous)
                        .stroke(
                            LinearGradient(
                                colors: [Color.white.opacity(0.2), Color.white.opacity(0.04)],
                                startPoint: .topLeading,
                                endPoint: .bottomTrailing
                            ),
                            lineWidth: 1
                        )
                )
                .shadow(color: Color.black.opacity(0.35), radius: 14, x: 0, y: 7)

            content
        }
    }
}

public struct ScaleButtonStyle: ButtonStyle {
    public func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.98 : 1.0)
            .animation(.easeInOut(duration: 0.15), value: configuration.isPressed)
    }
}

