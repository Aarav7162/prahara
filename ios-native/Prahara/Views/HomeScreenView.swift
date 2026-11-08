import SwiftUI

public struct HomeScreenView: View {
    @Binding var currentLocation: CityLocation
    let onSelectPachakkhan: (PachakkhanItem) -> Void
    @State private var now = Date()
    @State private var currentPage = 0
    let timer = Timer.publish(every: 1, on: .main, in: .common).autoconnect()

    public var body: some View {
        let solarTimes = SolarAstronomyEngine.calculateSolarTimes(
            date: now,
            latitude: currentLocation.latitude,
            longitude: currentLocation.longitude
        )
        let tithiInfo = JainTithiEngine.calculateTithi(date: now)

        ScrollView(showsIndicators: false) {
            VStack(spacing: 12) {
                // Location Pill
                HStack {
                    HStack(spacing: 6) {
                        Image(systemName: "location.fill")
                            .font(.system(size: 11))
                            .foregroundColor(.gray)
                        Text("\(currentLocation.name)\(currentLocation.isAutoDetected ? " • GPS Auto" : "")")
                            .font(.system(size: 12, weight: .semibold))
                            .foregroundColor(.white)
                    }
                    .padding(.horizontal, 12)
                    .padding(.vertical, 6)
                    .background(Color.white.opacity(0.1), in: Capsule())

                    Spacer()
                }

                // Parva Banner Card
                IOSCard {
                    HStack(spacing: 14) {
                        ZStack {
                            RoundedRectangle(cornerRadius: 14, style: .continuous)
                                .fill(Color.white.opacity(0.12))
                                .frame(width: 44, height: 44)
                            Image(systemName: "sun.max.fill")
                                .foregroundColor(.white)
                        }

                        VStack(alignment: .leading, spacing: 2) {
                            Text("\(tithiInfo.tithiName.uppercased()) TODAY •")
                                .font(.system(size: 14, weight: .bold))
                                .foregroundColor(.white)
                            Text(tithiInfo.isParva ? "Green vegetables restricted /" : "Standard Satvik observances /")
                                .font(.system(size: 12))
                                .foregroundColor(.gray)
                            Text(tithiInfo.isParva ? "Fasting observance" : "Chauvihar recommended")
                                .font(.system(size: 12))
                                .foregroundColor(.gray)
                        }
                        Spacer()
                    }
                    .padding(14)
                }

                // Split Hero Card
                IOSCard {
                    HStack {
                        VStack(alignment: .leading, spacing: 3) {
                            Text("Next Milestone")
                                .font(.system(size: 13, weight: .medium))
                                .foregroundColor(.gray)
                            Text("Navkarsi in")
                                .font(.system(size: 15, weight: .bold))
                                .foregroundColor(.white)
                            Text("24m 12s")
                                .font(.system(size: 26, weight: .heavy))
                                .foregroundColor(.white)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)

                        Rectangle()
                            .fill(Color.white.opacity(0.15))
                            .frame(width: 1, height: 50)

                        VStack(alignment: .leading, spacing: 3) {
                            Text("Local Tithi")
                                .font(.system(size: 11))
                                .foregroundColor(.gray)
                            Text(tithiInfo.fullName)
                                .font(.system(size: 13, weight: .bold))
                                .foregroundColor(.white)
                                .lineLimit(1)
                            Text("Sunrise–Sunset")
                                .font(.system(size: 11))
                                .foregroundColor(.gray)
                                .padding(.top, 2)
                            Text(formatTime(solarTimes.sunrise) + " - " + formatTime(solarTimes.sunset))
                                .font(.system(size: 12, weight: .semibold))
                                .foregroundColor(.white)
                        }
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.leading, 8)
                    }
                    .padding(16)
                }

                // Timings Section Title
                HStack {
                    Text("Pachakkhan Timings")
                        .font(.system(size: 15, weight: .semibold))
                        .foregroundColor(.gray)
                    Spacer()
                    Text("IST (UTC+5:30)")
                        .font(.system(size: 11, weight: .medium))
                        .foregroundColor(.gray.opacity(0.8))
                }
                .padding(.horizontal, 4)

                // 3-Column Grid
                LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())], spacing: 10) {
                    timingCardView(title: "Surya Uday", time: formatTime(solarTimes.sunrise), icon: "sunrise.fill", fraction: 1.0) {
                        if let item = PachakkhanRepository.getPachakkhan(type: .navkarsi) { onSelectPachakkhan(item) }
                    }
                    timingCardView(title: "Navkarsi", time: formatTime(solarTimes.navkarsi), icon: "sun.max.fill", fraction: 0.45, isActive: true) {
                        if let item = PachakkhanRepository.getPachakkhan(type: .navkarsi) { onSelectPachakkhan(item) }
                    }
                    timingCardView(title: "Porsi", time: formatTime(solarTimes.porsi), icon: "sparkles", fraction: 0.3) {
                        if let item = PachakkhanRepository.getPachakkhan(type: .porsi) { onSelectPachakkhan(item) }
                    }
                    timingCardView(title: "Purimaddh", time: formatTime(solarTimes.purimaddh), icon: "sun.haze.fill", fraction: 0.35) {
                        if let item = PachakkhanRepository.getPachakkhan(type: .purimaddh) { onSelectPachakkhan(item) }
                    }
                    timingCardView(title: "Sadh-Porsi", time: formatTime(solarTimes.sadhPorsi), icon: "clock.fill", fraction: 0.25) {
                        if let item = PachakkhanRepository.getPachakkhan(type: .sadhPorsi) { onSelectPachakkhan(item) }
                    }
                    timingCardView(title: "Chauvihar", time: formatTime(solarTimes.chauvihar), icon: "sunset.fill", fraction: 0.6) {
                        if let item = PachakkhanRepository.getPachakkhan(type: .chauvihar) { onSelectPachakkhan(item) }
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.top, 8)
            .padding(.bottom, 100)
        }
        .onReceive(timer) { _ in
            now = Date()
        }
    }

    private func formatTime(_ d: Date) -> String {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter.string(from: d)
    }

    private func timingCardView(title: String, time: String, icon: String, fraction: Double, isActive: Bool = false, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            ZStack {
                RoundedRectangle(cornerRadius: 22, style: .continuous)
                    .fill(LinearGradient(colors: [Color.white.opacity(0.08), Color.white.opacity(0.03)], startPoint: .topLeading, endPoint: .bottomTrailing))
                    .background(.ultraThinMaterial, in: RoundedRectangle(cornerRadius: 22, style: .continuous))
                    .overlay(
                        RoundedRectangle(cornerRadius: 22, style: .continuous)
                            .stroke(isActive ? Color.white.opacity(0.5) : Color.white.opacity(0.12), lineWidth: isActive ? 1.5 : 1)
                    )

                VStack(spacing: 8) {
                    ZStack {
                        Circle()
                            .fill(Color.white.opacity(0.12))
                            .frame(width: 32, height: 32)
                        Image(systemName: icon)
                            .font(.system(size: 14))
                            .foregroundColor(.white)
                    }

                    Text(title)
                        .font(.system(size: 11, weight: .medium))
                        .foregroundColor(.gray)

                    Text(time)
                        .font(.system(size: 16, weight: .bold))
                        .foregroundColor(.white)

                    GeometryReader { geo in
                        ZStack(alignment: .leading) {
                            Capsule().fill(Color.white.opacity(0.1)).frame(height: 3)
                            Capsule().fill(Color.white.opacity(0.6)).frame(width: geo.size.width * fraction, height: 3)
                        }
                    }
                    .frame(height: 3)
                    .padding(.horizontal, 10)
                }
                .padding(.vertical, 10)
            }
            .aspectRatio(0.86, contentMode: .fit)
        }
        .buttonStyle(ScaleButtonStyle())
    }
}

