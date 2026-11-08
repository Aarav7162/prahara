import SwiftUI

public struct ContentView: View {
    @State private var selectedTab = 0
    @State private var currentLocation = DefaultLocations.defaultCity
    @State private var selectedPachakkhan: PachakkhanItem? = nil

    public init() {}

    public var body: some View {
        ZStack(alignment: .bottom) {
            Color(red: 11/255, green: 13/255, blue: 18/255)
                .ignoresSafeArea()

            VStack(spacing: 0) {
                if selectedTab == 0 {
                    HomeScreenView(
                        currentLocation: $currentLocation,
                        onSelectPachakkhan: { item in
                            selectedPachakkhan = item
                        }
                    )
                } else if selectedTab == 1 {
                    Text("Jain Tithi Calendar View")
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                } else {
                    Text("Settings & Preferences")
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                }
            }

            // Floating Bottom Bar
            HStack(spacing: 30) {
                tabButton(icon: "house.fill", title: "Home", tag: 0)
                tabButton(icon: "calendar", title: "Calendar", tag: 1)
                tabButton(icon: "gearshape.fill", title: "Settings", tag: 2)
            }
            .padding(.horizontal, 24)
            .padding(.vertical, 10)
            .background(
                Capsule()
                    .fill(Color(red: 22/255, green: 27/255, blue: 38/255).opacity(0.92))
                    .overlay(Capsule().stroke(Color.white.opacity(0.15), lineWidth: 1))
            )
            .shadow(color: Color.black.opacity(0.4), radius: 16, x: 0, y: 8)
            .padding(.bottom, 12)
        }
    }

    private func tabButton(icon: String, title: String, tag: Int) -> some View {
        Button(action: { selectedTab = tag }) {
            VStack(spacing: 3) {
                Image(systemName: icon)
                    .font(.system(size: 18))
                Text(title)
                    .font(.system(size: 10, weight: .medium))
            }
            .foregroundColor(selectedTab == tag ? .white : .gray)
            .padding(.horizontal, 10)
        }
    }
}

