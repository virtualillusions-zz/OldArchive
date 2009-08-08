package com.fightingchaos.war.navigation;

import java.util.ArrayList;
import java.util.Iterator;

import com.jme.math.Vector3f;

/**
 * NavigationPath is a collection of waypoints that define a movement path for
 * an Actor. This object is ownded by an Actor and filled by
 * NavigationMesh::BuildNavigationPath().
 * 
 * Portions Copyright (C) Greg Snook, 2000
 * 
 * @author TR
 * 
 */
public class Path {
	public class WAYPOINT {
		public Vector3f Position; // 3D position of waypoint
		Cell Cell; // The cell which owns the waypoint

		@Override
		public String toString() {
			return "WAYPOINT: p:" + Position.x + "/" + Position.z + " c:"
					+ Cell;
		}
	};

	Mesh m_Parent;
	WAYPOINT m_StartPoint = new WAYPOINT();
	WAYPOINT m_EndPoint = new WAYPOINT();
	Iterator<WAYPOINT> m_EndID;
	public ArrayList<WAYPOINT> m_WaypointList = new ArrayList<WAYPOINT>();

	// : Setup
	// ----------------------------------------------------------------------------------------
	//
	// Sets up a new path from StartPoint to EndPoint. It adds the StartPoint as
	// the first
	// waypoint in the list and waits for further calls to AddWayPoint and
	// EndPath to
	// complete the list
	//
	// -------------------------------------------------------------------------------------://
	void Setup(Mesh Parent, Vector3f StartPoint, Cell StartCell,
			Vector3f EndPoint, Cell EndCell) {
		m_WaypointList.clear();

		m_Parent = Parent;
		m_StartPoint.Position = StartPoint;
		m_StartPoint.Cell = StartCell;
		m_EndPoint.Position = EndPoint;
		m_EndPoint.Cell = EndCell;

		// setup the waypoint list with our start and end points
		m_WaypointList.add(m_StartPoint);
	}

	// : AddWayPoint
	// ----------------------------------------------------------------------------------------
	//
	// Adds a new waypoint to the end of the list
	//
	// -------------------------------------------------------------------------------------://
	void AddWayPoint(Vector3f Point, Cell Cell) {
		WAYPOINT NewPoint = new WAYPOINT();

		NewPoint.Position = Point;
		NewPoint.Cell = Cell;

		m_WaypointList.add(NewPoint);
	}

	// : EndPath
	// ----------------------------------------------------------------------------------------
	//
	// Caps the end of the waypoint list by adding our final destination point
	//
	// -------------------------------------------------------------------------------------://
	void EndPath() {
		// cap the waypoint path with the last endpoint
		m_WaypointList.add(m_EndPoint);
	}

	Mesh Parent() {
		return (m_Parent);
	}

	WAYPOINT StartPoint() {
		return (m_StartPoint);
	}

	WAYPOINT EndPoint() {
		return (m_EndPoint);
	}

	public ArrayList<WAYPOINT> WaypointList() {
		return (m_WaypointList);
	}

	// : GetFurthestVisibleWayPoint
	// ----------------------------------------------------------------------------------------
	//
	// Find the furthest visible waypoint from the VantagePoint provided. This
	// is used to
	// smooth out irregular paths.
	//
	// -------------------------------------------------------------------------------------://
	public WAYPOINT GetFurthestVisibleWayPoint(WAYPOINT VantagePoint) {
		// see if we are already talking about the last waypoint
		if (VantagePoint == m_WaypointList.get(m_WaypointList.size() - 1)) {
			return (VantagePoint);
		}
		WAYPOINT Vantage = VantagePoint;
		int i = m_WaypointList.indexOf(VantagePoint);
		if(i < 0)
			return VantagePoint;
//		System.out.print("WAY IND:" + i);

		WAYPOINT TestWaypoint = VantagePoint;
		TestWaypoint = m_WaypointList.get(++i);

		if (TestWaypoint == m_WaypointList.get(m_WaypointList.size() - 1)) {
			System.out.println(" WAY IND was last");
			return (TestWaypoint);
		}

		WAYPOINT VisibleWaypoint = TestWaypoint;
		// TestWaypoint = m_WaypointList.get(++i);

		while (TestWaypoint != m_WaypointList.get(m_WaypointList.size() - 1)) {
			WAYPOINT Test = TestWaypoint;
			if (!m_Parent.LineOfSightTest(Vantage.Cell, Vantage.Position,
					Test.Position)) {
//				System.out.println(" WAY IND was:" + i);
				return (VisibleWaypoint);
			}
			VisibleWaypoint = TestWaypoint;
			TestWaypoint = m_WaypointList.get(++i);
		}
//		System.out.println(" WAY IND was:" + i);
		// the last
		return (TestWaypoint);
	}
}
